package com.hampcoders.glottia.platform.api.iam.domain.model.queries;

/**
 * Get user by email query.
 *
 * <p>This class represents the get user by email.
 *
 * @param email the email of the user.
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record GetUserByEmailQuery(String email) {}
