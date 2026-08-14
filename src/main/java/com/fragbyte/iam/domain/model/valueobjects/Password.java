package com.fragbyte.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Password value object.
 *
 * <p>
 *   Represents the password of the user.
 * </p>
 *
 * @param password the password of the user.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Embeddable
public record Password(
  String password
) {

  public Password {
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
  }
}