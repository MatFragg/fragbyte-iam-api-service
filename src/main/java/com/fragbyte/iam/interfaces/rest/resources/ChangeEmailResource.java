package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Change email resource.
 *
 * <p>This class represents the resource to change the email address of a user.
 *
 * @param email the new email of the user
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ChangeEmailResource(
    @NotBlank(message = "email is required") @Email(message = "email must be valid") String email) {}
