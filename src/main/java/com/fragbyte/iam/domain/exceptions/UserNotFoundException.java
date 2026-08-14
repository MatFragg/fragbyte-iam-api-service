package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when a user cannot be found by the provided identifier or email.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
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
