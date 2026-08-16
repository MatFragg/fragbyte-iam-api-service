package com.fragbyte.iam.interfaces.acl;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

import java.util.Set;

/**
 * Read model of a {@code User} exposed through the IAM anti-corruption layer to other bounded
 * contexts.
 *
 * @param userId the user identifier.
 * @param email the email of the user.
 * @param accessRoles the platform access roles of the user.
 * @param accountStatus the current account lifecycle state.
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserInfo(
    UserId userId, Email email, Set<AccessRoles> accessRoles, AccountStatus accountStatus) {}
