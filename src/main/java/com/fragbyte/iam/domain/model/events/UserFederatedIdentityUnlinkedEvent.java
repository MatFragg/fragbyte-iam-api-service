package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User federated identity unlinked event.
 *
 * <p>This class represents the event published when a federated identity is removed from a user
 * account.
 *
 * @param userId the identifier of the user.
 * @param provider the external authentication provider that was unlinked.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record UserFederatedIdentityUnlinkedEvent(
    UserId userId, AuthProvider provider, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user federated identity unlinked event constructor.
   *
   * @param userId the identifier of the user.
   * @param provider the external authentication provider that was unlinked.
   */
  public UserFederatedIdentityUnlinkedEvent(UserId userId, AuthProvider provider) {
    this(userId, provider, UUID.randomUUID().toString(), Instant.now());
  }
}
