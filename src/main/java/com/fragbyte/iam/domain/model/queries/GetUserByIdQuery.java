package com.hampcoders.glottia.platform.api.iam.domain.model.queries;

import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.UserId;

/**
 * Get user by id query.
 *
 * <p>This class represents the get user by id query.
 *
 * @param userId the identifier of the user.
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record GetUserByIdQuery(UserId userId) {}
