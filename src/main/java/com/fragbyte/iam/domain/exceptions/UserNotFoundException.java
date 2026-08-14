package com.hampcoders.glottia.platform.api.iam.domain.model.exceptions;

/** Thrown when a user cannot be found by the provided identifier or email. */
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param message the detail message
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
