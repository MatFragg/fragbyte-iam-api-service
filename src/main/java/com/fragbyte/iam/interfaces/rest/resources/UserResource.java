package com.fragbyte.iam.interfaces.rest.resources;

import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;

import java.util.Set;

/**
 * User resource.
 *
 * <p>This class represents the resource of the user.
 *
 * @param id the identifier of the user.
 * @param email the email of the user.
 * @param accountStatus the account lifecycle state.
 * @param roles the platform access roles of the user.
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserResource(String id, String email, AccountStatus accountStatus, Set<AccessRoles> roles) {}
