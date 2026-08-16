package com.fragbyte.iam.domain.model.valueobjects;

/**
 * Raw (plain text) password value object.
 *
 * <p>Represents a password as provided by the user during sign-in, sign-up or account management.
 * It is a transient value: it must never be persisted and must never be logged. Password strength
 * policy (e.g. minimum length) is enforced by the creation commands, not by this value object, so
 * that sign-in with a syntactically invalid password still surfaces as invalid credentials.
 *
 * @param password the raw password
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record RawPassword(String password) {

  /**
   * The value object constructor.
   *
   * @throws IllegalArgumentException if the password is null or blank
   */
  public RawPassword {
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
  }
}
