package de.unipa.hams.hamer.exception;

public class ShouldBeAuthenticatedException extends RuntimeException {

  public ShouldBeAuthenticatedException(String message) {
    super(message);
  }

  public ShouldBeAuthenticatedException(String message, Throwable cause) {
    super(message, cause);
  }
}
