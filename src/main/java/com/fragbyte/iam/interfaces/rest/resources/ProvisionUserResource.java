package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Provision user resource.
 *
 * <p>This class represents the resource to provision a user within the platform by an
 * administrator.
 *
 * @param email the email of the user
 * @param password the initial password of the user
 * @param roles the roles to grant to the user
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ProvisionUserResource(
    @NotBlank(message = "email is required") @Email(message = "email must be valid") String email,
    @NotBlank(message = "password is required")
        @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
        String password,
    @NotEmpty(message = "at least one role is required") Set<@NotBlank String> roles) {}
