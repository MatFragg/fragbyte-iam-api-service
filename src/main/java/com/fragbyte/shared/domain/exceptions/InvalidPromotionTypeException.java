package com.fragbyte.shared.domain.exceptions;

/**
 * Exception thrown when an invalid promotion type is provided.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class InvalidPromotionTypeException extends RuntimeException {
  /**
   * Constructs a new exception with a detail message.
   *
   * @param message the detail message
   */
  public InvalidPromotionTypeException(String message) {
    super(message);
  }
}
