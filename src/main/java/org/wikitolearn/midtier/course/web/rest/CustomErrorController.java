package org.wikitolearn.midtier.course.web.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.wikitolearn.midtier.course.entity.ErrorJson;

@RestController
public class CustomErrorController implements ErrorController {

  private static final String PATH = "/error";

  @Autowired
  private ErrorAttributes errorAttributes;

  @RequestMapping(value = PATH)
  ResponseEntity<ErrorJson> error(HttpServletRequest request, HttpServletResponse response) {
    return ResponseEntity.status(response.getStatus())
        .body(new ErrorJson(response.getStatus(), getErrorAttributes(request, true)));
  }

  @Override
  public String getErrorPath() {
    return PATH;
  }

  private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
    RequestAttributes requestAttributes = new ServletRequestAttributes(request);
    return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
  }
}