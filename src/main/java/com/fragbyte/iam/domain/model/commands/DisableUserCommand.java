package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Disable user command.
 *
 * <p>This class represents the command to disable a user account. A disabled account can no longer
 * sign in.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record DisableUserCommand(UserId userId) {}
