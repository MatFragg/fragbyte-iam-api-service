package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when an {@code UNVERIFIED} account attempts to sign in.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class AccountNotVerifiedException extends RuntimeException {

  /**
   * Constructs the exception with a friendly message.
   */
  public AccountNotVerifiedException() {
    super("The account has not been verified. Please verify your email address first.");
  }
}
