package com.hampcoders.glottia.platform.api.iam.domain.model.exceptions;

/** Thrown when sign-up attempts to register an email that already exists. */
public class EmailAlreadyExistsException extends RuntimeException {

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param message the detail message
   */
  public EmailAlreadyExistsException(String message) {
    super(message);
  }
}
