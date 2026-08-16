package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Unlock user command.
 *
 * <p>This class represents the command to unlock a user account, transitioning it back to {@code
 * ACTIVE}.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UnlockUserCommand(UserId userId) {}
