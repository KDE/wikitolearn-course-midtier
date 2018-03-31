package org.wikitolearn.midtier.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidResourceCreateException extends RuntimeException {
  
  private static final long serialVersionUID = -1612489332617776539L;
  
  public InvalidResourceCreateException() {
    super();
  
  }
  public InvalidResourceCreateException(String message, Throwable cause) {
      super(message, cause);
  }
  
  public InvalidResourceCreateException(String message) {
      super(message);
  }
  
  public InvalidResourceCreateException(Throwable cause) {
      super(cause);
  }

}
