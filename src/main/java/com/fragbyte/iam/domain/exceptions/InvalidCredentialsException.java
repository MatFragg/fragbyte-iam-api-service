package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when sign-in credentials do not match an existing user.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
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
