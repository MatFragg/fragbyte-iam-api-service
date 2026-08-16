package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User access role assigned event.
 *
 * <p>This class represents the user access role assigned event. It is published when a role is
 * granted to a {@code User}.
 *
 * @param userId the identifier of the user.
 * @param assignedRole the role that was granted.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserAccessRoleAssignedEvent(
    UserId userId, AccessRoles assignedRole, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user access role assigned event constructor.
   *
   * @param userId the identifier of the user.
   * @param assignedRole the role that was granted.
   */
  public UserAccessRoleAssignedEvent(UserId userId, AccessRoles assignedRole) {
    this(userId, assignedRole, UUID.randomUUID().toString(), Instant.now());
  }
}
