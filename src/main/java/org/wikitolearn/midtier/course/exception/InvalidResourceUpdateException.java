package org.wikitolearn.midtier.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidResourceUpdateException extends RuntimeException {
  
  private static final long serialVersionUID = -1612489332617776539L;
  
  public InvalidResourceUpdateException() {
    super();
  
  }
  public InvalidResourceUpdateException(String message, Throwable cause) {
      super(message, cause);
  }
  
  public InvalidResourceUpdateException(String message) {
      super(message);
  }
  
  public InvalidResourceUpdateException(Throwable cause) {
      super(cause);
  }

}
