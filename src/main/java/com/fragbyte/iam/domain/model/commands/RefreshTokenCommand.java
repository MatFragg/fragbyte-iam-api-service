package com.fragbyte.iam.domain.model.commands;

/**
 * Refresh token command.
 *
 * <p>This class represents the command to refresh the user's JWT.
 *
 * @param token the JWT presented for refresh (the one issued at sign-in/sign-up)
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record RefreshTokenCommand(String token) {}
