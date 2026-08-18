package com.fragbyte.iam.domain.exceptions;

/**
 * Cannot unlink last authentication method exception.
 *
 * <p>Thrown when attempting to unlink the only remaining authentication method from a user account.
 * A user must always retain at least one method to authenticate (either a password or at least one
 * federated identity).
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public class CannotUnlinkLastAuthMethodException extends RuntimeException {

  /** The cannot unlink last auth method exception constructor. */
  public CannotUnlinkLastAuthMethodException() {
    super("Cannot unlink the only remaining authentication method");
  }
}
