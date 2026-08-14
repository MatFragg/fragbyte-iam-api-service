package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User email changed event.
 *
 * <p>This class represents the user email changed event.
 *
 * @param userId the identifier of the user.
 * @param newEmail the new email of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserEmailChangedEvent(
  UserId userId, Email newEmail, String eventId, Instant occurredOn) implements DomainEvent {

  /**
   * The user email changed event constructor.
   *
   * @param userId the identifier of the user.
   * @param newEmail the new email of the user.
   */
  public UserEmailChangedEvent(UserId userId, Email newEmail) {
    this(userId, newEmail, UUID.randomUUID().toString(), Instant.now());
  }
}
