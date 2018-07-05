package org.wikitolearn.midtier.course.web.rest;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.wikitolearn.midtier.course.entity.ErrorJson;
import org.wikitolearn.midtier.course.entity.Page;
import org.wikitolearn.midtier.course.entity.dto.in.StoreOrUpdatePageDto;
import org.wikitolearn.midtier.course.service.PageService;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value =  "/pages")
public class PageController {
  
  @Autowired
  private PageService pageService;
  
  @Autowired ModelMapper modelMapper;
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success"),
    @ApiResponse(code = 404, message = "Not Found", response = ErrorJson.class)
  })
  @GetMapping(value = "/{pageId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Page getPage(@PathVariable String pageId) {
    return pageService.find(pageId, null);
  }
  
  @ApiResponses({
    @ApiResponse(code = 200, message = "Success", response = Page.class),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class),
    @ApiResponse(code = 422, message = "Uprocessable Entity")
  })
  @PatchMapping(value = "/{pageId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Page update(@PathVariable("pageId") String pageId, @Valid @RequestBody StoreOrUpdatePageDto page, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Page pageToUpdate = modelMapper.map(page, Page.class);
    pageToUpdate.setId(pageId);
    pageToUpdate.setEtag(etag);
    
    return pageService.update(pageToUpdate);
  }
  
  @ApiResponses({
    @ApiResponse(code = 204, message = "No Content"),
    @ApiResponse(code = 401, message = "Unauthorized"),
    @ApiResponse(code = 403, message = "Forbidden"),
    @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorJson.class)
  })
  @DeleteMapping(value = "/{pageId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("pageId") String pageId, @RequestHeader("If-Match") String etag) throws JsonProcessingException {
    Page page = new Page();
    page.setId(pageId);
    page.setEtag(etag);
    pageService.delete(page, false);
  }
}
