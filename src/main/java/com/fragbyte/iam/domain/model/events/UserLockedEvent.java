package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User locked event.
 *
 * <p>This class represents the user locked event. It is published when an account transitions to
 * {@code LOCKED}, either by an administrator or automatically after too many failed sign-in
 * attempts.
 *
 * @param userId the identifier of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserLockedEvent(UserId userId, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user locked event constructor.
   *
   * @param userId the identifier of the user.
   */
  public UserLockedEvent(UserId userId) {
    this(userId, UUID.randomUUID().toString(), Instant.now());
  }
}
