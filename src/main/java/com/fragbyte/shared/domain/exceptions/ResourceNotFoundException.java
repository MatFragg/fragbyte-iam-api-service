package com.fragbyte.shared.domain.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class ResourceNotFoundException extends RuntimeException {
  /**
   * Constructs a new exception with a detail message.
   *
   * @param message the detail message
   */
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
