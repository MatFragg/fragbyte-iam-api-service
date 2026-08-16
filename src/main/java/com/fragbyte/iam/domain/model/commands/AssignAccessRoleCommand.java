package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Assign access role command.
 *
 * <p>This class represents the command to grant an access role to a user. It is issued by an
 * administrator; it is never part of a self-service flow.
 *
 * @param userId the identifier of the user.
 * @param accessRole the role to grant.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record AssignAccessRoleCommand(UserId userId, AccessRoles accessRole) {}
