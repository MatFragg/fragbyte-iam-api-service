package com.hampcoders.glottia.platform.api.iam.domain.model.events;

import com.hampcoders.glottia.platform.api.shared.domain.model.events.DomainEvent;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.UserId;
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
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record UserEmailChangedEvent(
    UserId userId, String newEmail, String eventId, Instant occurredOn) implements DomainEvent {

  /**
   * The user email changed event constructor.
   *
   * @param userId the identifier of the user.
   * @param newEmail the new email of the user.
   */
  public UserEmailChangedEvent(UserId userId, String newEmail) {
    this(userId, newEmail, UUID.randomUUID().toString(), Instant.now());
  }
}
