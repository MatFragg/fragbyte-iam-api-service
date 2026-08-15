package com.fragbyte.iam.interfaces.rest.resources;

/**
 * User resource.
 *
 * <p>This class represents the resource the user.
 *
 * @param id the identifier of the user.
 * @param email the email of the user.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record UserResource(String id, String email) {}
