package com.fragbyte.iam.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * Value object representing a user identifier.
 *
 * <p>User IDs follow the format {@code us-} followed by a UUID.
 *
 * @param value the string representation of the user ID (must start with "us-")
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Embeddable
public record UserId(@Column(name = "id") @JsonValue String value) implements Serializable {
  /**
   * Creates a new unique UserId.
   *
   * @return a new UserId with an "us-" prefix
   */
  public static UserId newUserId() {
    return new UserId("us-" + UUID.randomUUID());
  }

  /**
   * Compact constructor that validates the value format.
   *
   * @param value the user ID string (must start with "us-")
   */
  public UserId {
    if (value == null || !value.startsWith("us-")) {
      throw new IllegalArgumentException("Invalid UserId: " + value);
    }
  }
}