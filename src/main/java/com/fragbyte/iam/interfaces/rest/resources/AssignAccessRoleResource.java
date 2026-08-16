package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Assign access role resource.
 *
 * <p>This class represents the resource to grant a platform access role to a user.
 *
 * @param role the role to grant
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record AssignAccessRoleResource(
    @NotBlank(message = "role is required") String role) {}
