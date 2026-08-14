package com.fragbyte.iam.domain.model.commands;

/**
 * Refresh token command.
 *
 * <p>This class represents the command to refresh the user's token.
 *
 * @param userId the user id of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record RefreshTokenCommand(String userId) {}
