package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Lock user command.
 *
 * <p>This class represents the command to lock a user account. An administrator issues this command
 * to temporarily suspend access to the platform.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record LockUserCommand(UserId userId) {}
