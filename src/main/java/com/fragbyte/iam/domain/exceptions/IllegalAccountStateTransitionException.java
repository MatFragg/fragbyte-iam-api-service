package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when a guarded account status transition is not permitted by the current state.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class IllegalAccountStateTransitionException extends RuntimeException {

  /**
   * Constructs the exception with a descriptive message.
   *
   * @param message the detail message
   */
  public IllegalAccountStateTransitionException(String message) {
    super(message);
  }
}
