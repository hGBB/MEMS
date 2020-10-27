package de.unipa.hams.hamer.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
  @ExceptionHandler(McRegistrationException.class)
  public ErrorDTO handleMcRegistrationException(McRegistrationException e) {
    log.debug(ExceptionUtils.getStackTrace(e));
    return new ErrorDTO(e.getMessage());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void handleEntityNotFoundException(EntityNotFoundException e) {
  }

  @ExceptionHandler(NodeDeleteException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDTO handleNodeDeleteException(NodeDeleteException e) {
    return new ErrorDTO(e.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleAccessDeniedException(AccessDeniedException e) {
    log.warn(ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(ShouldBeAuthenticatedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleAccessDeniedException(ShouldBeAuthenticatedException e) {
    log.warn(ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(AuthorizationContextException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public void handleAccessDeniedException(AuthorizationContextException e) {
    log.warn(ExceptionUtils.getStackTrace(e));
  }

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorDTO handleGeneral(Throwable throwable) {
    log.error(ExceptionUtils.getStackTrace(throwable));
    return new ErrorDTO(throwable.getMessage());
  }
}
