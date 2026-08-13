package com.fragbyte.shared.domain.exceptions;

/**
 * Exception thrown when an operation is forbidden for the current user.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class ForbiddenException extends RuntimeException {
  /**
   * Constructs a new exception with a detail message.
   *
   * @param message the detail message
   */
  public ForbiddenException(String message) {
    super(message);
  }
}
