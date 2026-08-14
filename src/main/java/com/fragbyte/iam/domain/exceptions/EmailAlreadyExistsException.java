package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when sign-up attempts to register an email that already exists.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
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
