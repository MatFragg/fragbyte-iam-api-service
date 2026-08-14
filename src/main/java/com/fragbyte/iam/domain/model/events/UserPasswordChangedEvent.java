package com.hampcoders.glottia.platform.api.iam.domain.model.events;

import com.hampcoders.glottia.platform.api.shared.domain.model.events.DomainEvent;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.UserId;
import java.time.Instant;
import java.util.UUID;

/**
 * User password changed event.
 *
 * <p>This class represents the user password changed event.
 *
 * @param userId the identifier of the user.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record UserPasswordChangedEvent(UserId userId, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user password changed event constructor.
   *
   * @param userId the identifier of the user.
   */
  public UserPasswordChangedEvent(UserId userId) {
    this(userId, UUID.randomUUID().toString(), Instant.now());
  }
}
