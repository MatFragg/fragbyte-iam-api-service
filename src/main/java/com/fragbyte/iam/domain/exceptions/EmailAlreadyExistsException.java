package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when sign-up attempts to register an email that already exists.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class EmailAlreadyExistsException extends RuntimeException {

  /**
   * Constructs the exception with a friendly message.
   *
   * @param email the email address that is already registered
   */
  public EmailAlreadyExistsException(String email) {
    super("The email " + email + " is already registered.");
  }
}
