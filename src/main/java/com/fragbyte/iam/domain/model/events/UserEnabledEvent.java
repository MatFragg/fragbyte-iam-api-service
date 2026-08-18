package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User enabled event.
 *
 * <p>This class represents the user enabled event. It is published when a {@code DISABLED} account
 * transitions back to {@code ACTIVE}.
 *
 * @param userId the identifier of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record UserEnabledEvent(UserId userId, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user enabled event constructor.
   *
   * @param userId the identifier of the user.
   */
  public UserEnabledEvent(UserId userId) {
    this(userId, UUID.randomUUID().toString(), Instant.now());
  }
}
