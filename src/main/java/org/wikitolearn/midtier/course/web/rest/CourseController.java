package org.wikitolearn.midtier.course.web.rest;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.ErrorJson;
import org.wikitolearn.midtier.course.entity.dto.in.AddCourseChaptersDto;
import org.wikitolearn.midtier.course.entity.dto.in.GetCourseDto;
import org.wikitolearn.midtier.course.entity.dto.in.GetCoursesDto;
import org.wikitolearn.midtier.course.entity.dto.in.StoreCourseDto;
import org.wikitolearn.midtier.course.entity.dto.in.UpdateCourseChaptersDto;
import org.wikitolearn.midtier.course.entity.dto.in.UpdateCourseDto;
import org.wikitolearn.midtier.course.entity.dto.out.AddedCourseChaptersDto;
import org.wikitolearn.midtier.course.entity.dto.out.StoredOrUpdatedCourseDto;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.service.ChapterService;
import org.wikitolearn.midtier.course.service.CourseService;
import org.wikitolearn.midtier.course.service.PageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
  
  @Autowired
  private ObjectMapper objectMapper;
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = GetCoursesDto.class)
  })
  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public GetCoursesDto getCourses(@RequestParam(value="page", required=false) Integer page) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", String.valueOf(page));
    
    EntityList<Course> courses = courseService.findAll(params);
    
    return modelMapper.map(courses, GetCoursesDto.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = GetCourseDto.class)
  })
  @GetMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public GetCourseDto getCourse(@PathVariable String courseId) throws JsonProcessingException {
    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    ObjectNode projectionJsonObject = objectMapper.getNodeFactory().objectNode().put("title", 1).put("pages", 1);
    chapterParams.add("projection", objectMapper.writeValueAsString(projectionJsonObject));

    MultiValueMap<String, String> pagesParams = new LinkedMultiValueMap<>();
    projectionJsonObject = objectMapper.getNodeFactory().objectNode().put("title", 1);
    pagesParams.add("projection", objectMapper.writeValueAsString(projectionJsonObject));

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
  
  @ApiResponses({
    @ApiResponse(code = 201, message = "Created", response = StoredOrUpdatedCourseDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class),
    @ApiResponse(code = 422, message = "Unprocessable Entity")
  })
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public StoredOrUpdatedCourseDto storeCourse(@Valid @RequestBody StoreCourseDto course) throws JsonProcessingException {
    Course storedCourse = courseService.save(modelMapper.map(course, Course.class));
    return modelMapper.map(storedCourse, StoredOrUpdatedCourseDto.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = StoredOrUpdatedCourseDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class),
    @ApiResponse(code = 422, message = "Unprocessable Entity")
  })
  @PatchMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public StoredOrUpdatedCourseDto updateCourse(@PathVariable("courseId") String courseId, @RequestBody UpdateCourseDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.update(courseToUpdate);
    return modelMapper.map(updatedCourse, StoredOrUpdatedCourseDto.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 204, message = "No Content"),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @DeleteMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCourse(@PathVariable("courseId") String courseId, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Course course = new Course();
    course.setId(courseId);
    course.setEtag(etag);
    courseService.delete(course);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = StoredOrUpdatedCourseDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class),
    @ApiResponse(code = 422, message = "Unprocessable Entity")
  })
  @PatchMapping(value = "/{courseId}/chapters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public StoredOrUpdatedCourseDto updateChapters(@PathVariable("courseId") String courseId, @RequestBody UpdateCourseChaptersDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.updateChapters(courseToUpdate);
    return modelMapper.map(updatedCourse, StoredOrUpdatedCourseDto.class); 
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = AddedCourseChaptersDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class),
    @ApiResponse(code = 422, message = "Unprocessable Entity")
  })
  @PostMapping(value = "/{courseId}/chapters", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public AddedCourseChaptersDto addChapters(@PathVariable("courseId") String courseId, @RequestBody AddCourseChaptersDto course, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Course courseToUpdate = modelMapper.map(course, Course.class);
    courseToUpdate.setId(courseId);
    courseToUpdate.setEtag(etag);
    
    Course updatedCourse = courseService.addChapters(courseToUpdate);
    return modelMapper.map(updatedCourse, AddedCourseChaptersDto.class);
  }
}
