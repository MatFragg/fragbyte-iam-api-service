package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.UUID;

/**
 * User access role changed event.
 *
 * <p>This class represents the user access role changed event. It is published when an access role
 * is added to or removed from a {@code User}.
 *
 * @param userId the identifier of the user.
 * @param previousRole the previously removed role, or {@code null} when a role was added.
 * @param newRole the newly added role, or {@code null} when a role was removed.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 *
 * @see com.fragbyte.iam.domain.model.aggregates.User
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserAccessRoleChangedEvent(
  UserId userId, AccessRoles previousRole, AccessRoles newRole, String eventId, Instant occurredOn)
  implements DomainEvent {

  /**
   * The user access role changed event constructor.
   *
   * @param userId the identifier of the user.
   * @param previousRole the previously removed role, or {@code null} when a role was added.
   * @param newRole the newly added role, or {@code null} when a role was removed.
   */
  public UserAccessRoleChangedEvent(UserId userId, AccessRoles previousRole, AccessRoles newRole) {
    this(userId, previousRole, newRole, UUID.randomUUID().toString(), Instant.now());
  }
}
