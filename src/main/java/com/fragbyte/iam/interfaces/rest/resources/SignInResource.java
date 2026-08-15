package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Sign in resource.
 *
 * <p>This class represents the resource to sign-in within the platform
 *
 * @param email the email of the user
 * @param password the password of the user
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record SignInResource(
    @NotBlank(message = "email is required") @Email(message = "email must be valid") String email,
    @NotBlank(message = "password is required") String password) {}
