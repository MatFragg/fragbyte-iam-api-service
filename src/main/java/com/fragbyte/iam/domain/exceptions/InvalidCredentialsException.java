package com.hampcoders.glottia.platform.api.iam.domain.model.exceptions;

/** Thrown when sign-in credentials do not match an existing user. */
public class InvalidCredentialsException extends RuntimeException {

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param message the detail message
   */
  public InvalidCredentialsException(String message) {
    super(message);
  }
}
