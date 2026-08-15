package com.fragbyte.iam.interfaces.rest.resources;

/**
 * Authenticated user resource.
 *
 * <p>This class represents the resource to authenticate a user
 *
 * @param id the identifier of the user.
 * @param email the email of the user.
 * @param token the authentication token.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record AuthenticatedUserResource(String id, String email, String token) {}
