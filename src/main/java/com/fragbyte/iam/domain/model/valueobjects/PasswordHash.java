package com.fragbyte.iam.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * Password hash value object.
 *
 * <p>Represents a one-way encoded password along with the {@link HashingAlgorithm} that produced
 * it. Only the hash value is ever persisted; the raw password never reaches the storage layer.
 *
 * @param value the encoded password hash
 * @param algorithm the algorithm used to produce the hash
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Embeddable
public record PasswordHash(
    @Column(name = "password_hash", nullable = false) String value,
    @Enumerated(EnumType.STRING)
        @Column(name = "hashing_algorithm", nullable = false, length = 20)
        HashingAlgorithm algorithm) {

  /**
   * The value object constructor.
   *
   * @throws IllegalArgumentException if the hash or the algorithm is null/blank
   */
  public PasswordHash {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Password hash cannot be null or blank");
    }
    if (algorithm == null) {
      throw new IllegalArgumentException("Hashing algorithm cannot be null");
    }
  }
}
