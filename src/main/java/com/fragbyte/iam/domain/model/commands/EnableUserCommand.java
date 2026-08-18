package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Enable user command.
 *
 * <p>This class represents the command to re-enable a previously disabled user account.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record EnableUserCommand(UserId userId) {}
