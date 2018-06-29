package org.wikitolearn.midtier.course.web.rest;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.dto.AddCourseChaptersDto;
import org.wikitolearn.midtier.course.entity.dto.GetCourseDto;
import org.wikitolearn.midtier.course.entity.dto.GetCoursesDto;
import org.wikitolearn.midtier.course.entity.dto.UpdateCourseChaptersDto;
import org.wikitolearn.midtier.course.entity.dto.UpdateCourseDto;
import org.wikitolearn.midtier.course.entity.dto.UpdatedCourseDto;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.service.ChapterService;
import org.wikitolearn.midtier.course.service.CourseService;
import org.wikitolearn.midtier.course.service.PageService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/courses")
public class CourseController {
  
  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private CourseService courseService;

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private PageService pageService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public GetCoursesDto getCourses(@RequestParam(value="page", required=false) String page) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", page);
    
    EntityList<Course> courses = courseService.findAll(params);
    
    return modelMapper.map(courses, GetCoursesDto.class);
  }

  @GetMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public GetCourseDto getCourse(@PathVariable String courseId) {
    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    chapterParams.add("projection", "{\"title\":1, \"pages\":1}");

    MultiValueMap<String, String> pagesParams = new LinkedMultiValueMap<>();
    pagesParams.add("projection", "{\"title\":1}");

    Course course = courseService.find(courseId, null);
    course.setChapters(
        Optional
        .ofNullable(course.getChapters())
        .orElseGet(Collections::emptyList)
        .parallelStream()
        .map(chapter -> {
          chapter = chapterService.find(chapter.getId(), chapterParams);
          chapter.setPages(
              Optional
              .ofNullable(chapter.getPages())
              .orElseGet(Collections::emptyList)
              .parallelStream()
              .map(page -> pageService.find(page.getId(), pagesParams))
              .collect(Collectors.toList()));
          return chapter;
        })
        .collect(Collectors.toList()));
    return modelMapper.map(course, GetCourseDto.class);
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<Course> storeCourse(@RequestBody Course course) throws JsonProcessingException {
    ResponseEntity<Course> response = new ResponseEntity<>(courseService.save(course), HttpStatus.CREATED);
    return response;
  }

  @PatchMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UpdatedCourseDto updateCourse(@PathVariable("courseId") String courseId, @RequestBody UpdateCourseDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.update(courseToUpdate);
    return modelMapper.map(updatedCourse, UpdatedCourseDto.class);
  }

  @DeleteMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Course deleteCourse(@PathVariable("courseId") String courseId, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Course course = new Course();
    course.setId(courseId);
    course.setEtag(etag);
    
    return courseService.delete(course);
  }

  @PatchMapping(value = "/{courseId}/chapters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UpdatedCourseDto updateChapters(@PathVariable("courseId") String courseId, @RequestBody UpdateCourseChaptersDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.updateChapters(courseToUpdate);
    return modelMapper.map(updatedCourse, UpdatedCourseDto.class); 
  }

  @PostMapping(value = "/{courseId}/chapters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UpdatedCourseDto addChapters(@PathVariable("courseId") String courseId, @RequestBody AddCourseChaptersDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.addChapters(courseToUpdate);
    return modelMapper.map(updatedCourse, UpdatedCourseDto.class);
  }
}
