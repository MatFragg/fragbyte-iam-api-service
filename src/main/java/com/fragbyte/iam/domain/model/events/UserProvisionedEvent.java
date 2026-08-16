package com.fragbyte.iam.domain.model.events;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.events.DomainEvent;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * User provisioned event.
 *
 * <p>This class represents the user provisioned event. It is published when an administrator
 * creates a {@code User} on behalf of the platform, as opposed to a self-service sign-up.
 *
 * @param userId the identifier of the user.
 * @param email the email of the user.
 * @param accessRoles the roles granted at provisioning time.
 * @param eventId the event identifier.
 * @param occurredOn the instant when the event occurred on.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserProvisionedEvent(
    UserId userId, Email email, Set<AccessRoles> accessRoles, String eventId, Instant occurredOn)
    implements DomainEvent {

  /**
   * The user provisioned event constructor.
   *
   * @param userId the identifier of the user.
   * @param email the email of the user.
   * @param accessRoles the roles granted at provisioning time.
   */
  public UserProvisionedEvent(UserId userId, Email email, Set<AccessRoles> accessRoles) {
    this(userId, email, accessRoles, UUID.randomUUID().toString(), Instant.now());
  }
}
