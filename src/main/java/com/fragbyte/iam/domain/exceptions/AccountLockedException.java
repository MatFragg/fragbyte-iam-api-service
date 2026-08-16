package com.fragbyte.iam.domain.exceptions;

/**
 * Thrown when a {@code LOCKED} account attempts to sign in.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class AccountLockedException extends RuntimeException {

  /**
   * Constructs the exception with a friendly message.
   */
  public AccountLockedException() {
    super("The account is locked. Contact an administrator to unlock it.");
  }
}
