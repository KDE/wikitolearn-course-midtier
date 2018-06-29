package org.wikitolearn.midtier.course.web.rest;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.Course;
import org.wikitolearn.midtier.course.entity.EntityList;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.exception.ResourceNotFoundException;
import org.wikitolearn.midtier.course.service.ChapterService;
import org.wikitolearn.midtier.course.service.CourseService;
import org.wikitolearn.midtier.course.service.PageService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/history/courses")
public class HistoryController {

  @Autowired
  private CourseService courseService;

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private PageService pageService;
  
  @ApiOperation(value = "getCourseVersion")
  @GetMapping(value = "/{courseId}/versions/{version}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Course getCourseVersion(@PathVariable(value = "courseId", required = true) String courseId,
      @PathVariable(value = "version", required = true) String version) {
    MultiValueMap<String, String> courseParams = new LinkedMultiValueMap<>();
    courseParams.add("version", version);

    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    chapterParams.add("projection", "{\"title\":1, \"pages\":1}");
    chapterParams.add("version", "");

    MultiValueMap<String, String> pagesParams = new LinkedMultiValueMap<>();
    pagesParams.add("projection", "{\"title\":1}");
    pagesParams.add("version", "");

    Course course = courseService.find(courseId, courseParams);
    course.setChapters(
        Optional
        .ofNullable(course.getChapters())
        .orElseGet(Collections::emptyList)
        .parallelStream()
        .map(chapter -> {
          chapterParams.set("version", String.valueOf(chapter.getVersion()));
          chapter = chapterService.find(chapter.getId(), chapterParams);
          chapter.setPages(
              Optional
              .ofNullable(chapter.getPages())
              .orElseGet(Collections::emptyList)
              .parallelStream()
              .map(page -> {
                pagesParams.set("version", String.valueOf(page.getVersion()));
                return pageService.find(page.getId(), pagesParams);
              })
              .collect(Collectors.toList()));
          return chapter;
        })
        .collect(Collectors.toList()));
    return course;
  }
  
  @GetMapping(value = "/{courseId}/versions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public EntityList<Course> getCourseVersions(@PathVariable(value = "courseId", required = true) String courseId,
      @RequestParam(value="page", required=false) String page) {
    MultiValueMap<String, String> courseParams = new LinkedMultiValueMap<>();
    courseParams.add("page", page);
    courseParams.add("version", "all");

    EntityList<Course> courseVersions = courseService.getAllCourseVersions(courseId, courseParams);
    return courseVersions;
  }
  
  @GetMapping(value = "/{courseId}/versions/{version}", params = "pageId", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Page getPage(@PathVariable(value = "courseId", required = true) String courseId,
      @PathVariable(value = "version", required = true) String version,
      @RequestParam(value = "pageId", required = true) String pageId) {

    MultiValueMap<String, String> courseParams = new LinkedMultiValueMap<>();
    courseParams.add("version", version);

    MultiValueMap<String, String> chapterParams = new LinkedMultiValueMap<>();
    chapterParams.add("projection", "{\"title\":1, \"pages\":1}");
    chapterParams.add("version", "");

    MultiValueMap<String, String> pageParams = new LinkedMultiValueMap<>();
    pageParams.add("version", "");

    Course course = courseService.find(courseId, courseParams);
    Chapter chapterTarget = 
        Optional
        .ofNullable(course.getChapters())
        .orElseGet(Collections::emptyList)
        .parallelStream()
        .filter(chapter -> {
          chapterParams.set("version", String.valueOf(chapter.getVersion()));
          Chapter enrichedChapter = chapterService.find(chapter.getId(), chapterParams);
          
          boolean isPagePresent = Optional.ofNullable(enrichedChapter.getPages()).orElseGet(Collections::emptyList)
              .parallelStream().filter(page -> pageId.equals(page.getId())).findFirst().isPresent();
          
          if (!isPagePresent) {
            return false; 
          }
          
          chapter.setPages(enrichedChapter.getPages());
          return true;
        })
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException());
    
    return Optional.ofNullable(chapterTarget.getPages()).orElseGet(Collections::emptyList).parallelStream()
        .filter(page -> pageId.equals(page.getId()))
        .findFirst()
        .map(page -> {
          pageParams.set("version", String.valueOf(page.getVersion()));
          return pageService.find(page.getId(), pageParams);
        })
        .orElseThrow(() -> new ResourceNotFoundException());
  }
}
