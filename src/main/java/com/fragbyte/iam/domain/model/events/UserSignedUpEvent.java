package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User signed up event.
 *
 * <p>This class represents the user signed up event. It is published when a new {@code User}
 * aggregate is created with the default access role.
 *
 * @param userId the identifier of the user.
 * @param email the email of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 *
 * @see com.fragbyte.iam.domain.model.aggregates.User
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserSignedUpEvent(UserId userId, Email email, String eventId, Instant occurredOn)
  implements DomainEvent {

  /**
   * The user signed up event constructor.
   *
   * @param userId the identifier of the user.
   * @param email the email of the user.
   */
  public UserSignedUpEvent(UserId userId, Email email) {
    this(userId, email, UUID.randomUUID().toString(), Instant.now());
  }
}
