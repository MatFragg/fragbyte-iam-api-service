package com.fragbyte.iam.domain.model.queries;

import com.fragbyte.iam.domain.model.valueobjects.Email;

/**
 * Get user by email query.
 *
 * <p>This class represents the get user by email.
 *
 * @param email the email of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record GetUserByEmailQuery(Email email) {}
