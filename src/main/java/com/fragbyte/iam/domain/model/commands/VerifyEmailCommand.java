package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Verify email command.
 *
 * <p>This class represents the command to verify the email address of a user, transitioning the
 * account from {@code UNVERIFIED} to {@code ACTIVE}.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record VerifyEmailCommand(UserId userId) {}
