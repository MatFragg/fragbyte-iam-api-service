package com.hampcoders.glottia.platform.api.iam.domain.model.commands;

/**
 * Refresh token command.
 *
 * <p>This class represents the command to refresh the user's token.
 *
 * @param userId the user id of the user.
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record RefreshTokenCommand(String userId) {}
