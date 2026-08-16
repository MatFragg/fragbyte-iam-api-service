package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Remove access role command.
 *
 * <p>This class represents the command to revoke an access role from a user. A user must always
 * retain at least one access role.
 *
 * @param userId the identifier of the user.
 * @param accessRole the role to revoke.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record RemoveAccessRoleCommand(UserId userId, AccessRoles accessRole) {}
