package org.wikitolearn.midtier.course.web.rest;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.Chapter;
import org.wikitolearn.midtier.course.entity.ErrorJson;
import org.wikitolearn.midtier.course.entity.dto.in.AddChapterPagesDto;
import org.wikitolearn.midtier.course.entity.dto.in.UpdateChapterDto;
import org.wikitolearn.midtier.course.entity.dto.in.UpdateChapterPagesDto;
import org.wikitolearn.midtier.course.entity.dto.out.AddedChapterPagesDto;
import org.wikitolearn.midtier.course.entity.dto.out.UpdatedChapter;
import org.wikitolearn.midtier.course.exception.InvalidResourceCreateException;
import org.wikitolearn.midtier.course.service.ChapterService;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value =  "/chapters")
public class ChapterController {
  
  @Autowired
  private ChapterService chapterService;
  
  @Autowired
  private ModelMapper modelMapper;
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = UpdatedChapter.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 422, message = "Uprocessable Entity"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @PatchMapping(value = "/{chapterId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UpdatedChapter update(@PathVariable("chapterId") String chapterId, @Valid @RequestBody UpdateChapterDto chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Chapter chapterToUpdate = modelMapper.map(chapter, Chapter.class);
    chapterToUpdate.setId(chapterId);
    chapterToUpdate.setEtag(etag);
    
    Chapter updatedChapter = chapterService.update(chapterToUpdate);
    
    return modelMapper.map(updatedChapter, UpdatedChapter.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = UpdateChapterPagesDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 422, message = "Uprocessable Entity"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @PatchMapping(value = "/{chapterId}/pages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public UpdateChapterPagesDto updatePages(@PathVariable("chapterId") String chapterId, @Valid @RequestBody UpdateChapterPagesDto chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Chapter chapterToUpdate = modelMapper.map(chapter, Chapter.class);
    chapterToUpdate.setId(chapterId);
    chapterToUpdate.setEtag(etag);
    
    Chapter updatedChapter = chapterService.updatePages(chapterToUpdate);
    return modelMapper.map(updatedChapter, UpdateChapterPagesDto.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = AddedChapterPagesDto.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 422, message = "Uprocessable Entity"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @PostMapping(value = "/{chapterId}/pages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public AddedChapterPagesDto addPages(@PathVariable("chapterId") String chapterId, @Valid @RequestBody AddChapterPagesDto chapter, @RequestHeader("If-Match") String etag) throws JsonProcessingException, InvalidResourceCreateException {
    Chapter chapterToUpdate = modelMapper.map(chapter, Chapter.class);
    chapterToUpdate.setId(chapterId);
    chapterToUpdate.setEtag(etag);

    Chapter updatedChapter = chapterService.addPages(chapterToUpdate);
    return modelMapper.map(updatedChapter, AddedChapterPagesDto.class);
  }
  
  @ApiResponses({
    @ApiResponse(code = 204, message = "No Content"),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @DeleteMapping(value = "/{chapterId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("chapterId") String chapterId, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Chapter chapter = new Chapter();
    chapter.setId(chapterId);
    chapter.setEtag(etag);
    chapterService.delete(chapter, false);
  }
}
