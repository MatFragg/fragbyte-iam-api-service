package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when a {@code DISABLED} account attempts to sign in.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class AccountDisabledException extends RuntimeException {

  /**
   * Constructs the exception with a friendly message.
   */
  public AccountDisabledException() {
    super("The account is disabled.");
  }
}
