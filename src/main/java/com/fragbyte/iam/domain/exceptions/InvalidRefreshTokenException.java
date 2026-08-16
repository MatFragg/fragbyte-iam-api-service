package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when a JWT cannot be used to refresh a token.
 *
 * <p>Surfaces when the presented token is malformed, forged, expired beyond recovery, or belongs to
 * a user that no longer exists.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class InvalidRefreshTokenException extends RuntimeException {

  /**
   * Constructs the exception with a friendly message.
   */
  public InvalidRefreshTokenException() {
    super("The provided refresh token is invalid or expired.");
  }
}
