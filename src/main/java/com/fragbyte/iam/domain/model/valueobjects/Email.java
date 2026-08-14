package com.fragbyte.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Email value object.
 *
 * <p>
 *   Represents the email of the user.
 * </p>
 *
 * @param email the email of the user.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Embeddable
public record Email(
  @Column(nullable = false, unique = true)
  String email
) {

  /**
   * The value object constructor.
   */
  public Email {
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email address cannot be null or empty");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
      throw new IllegalArgumentException("Invalid email address format");
    }
  }
}