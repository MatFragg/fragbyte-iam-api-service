package com.fragbyte.iam.domain.model.queries;

import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Get user by id query.
 *
 * <p>This class represents the get user by id query.
 *
 * @param userId the identifier of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record GetUserByIdQuery(UserId userId) {}
