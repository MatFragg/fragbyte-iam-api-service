package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User access role removed event.
 *
 * <p>This class represents the user access role removed event. It is published when a role is
 * revoked from a {@code User}.
 *
 * @param userId the identifier of the user.
 * @param removedRole the role that was revoked.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserAccessRoleRemovedEvent(
    UserId userId, AccessRoles removedRole, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user access role removed event constructor.
   *
   * @param userId the identifier of the user.
   * @param removedRole the role that was revoked.
   */
  public UserAccessRoleRemovedEvent(UserId userId, AccessRoles removedRole) {
    this(userId, removedRole, UUID.randomUUID().toString(), Instant.now());
  }
}
