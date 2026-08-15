package com.fragbyte.iam.interfaces.rest.resources;

/**
 * Refreshed Token resource.
 *
 * <p>This class represents the resource of the refreshed token within the platform
 *
 * @param token the refreshed token of the user
 * @param email the email of the user
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record RefreshedTokenResource(String token, String email) {}
