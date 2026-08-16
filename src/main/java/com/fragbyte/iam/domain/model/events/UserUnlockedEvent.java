package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User unlocked event.
 *
 * <p>This class represents the user unlocked event. It is published when a {@code LOCKED} account
 * transitions back to {@code ACTIVE}.
 *
 * @param userId the identifier of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserUnlockedEvent(UserId userId, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user unlocked event constructor.
   *
   * @param userId the identifier of the user.
   */
  public UserUnlockedEvent(UserId userId) {
    this(userId, UUID.randomUUID().toString(), Instant.now());
  }
}
