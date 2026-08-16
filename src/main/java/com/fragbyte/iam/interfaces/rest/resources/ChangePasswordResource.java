package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Change password resource.
 *
 * <p>This class represents the resource to change the password of a user.
 *
 * @param currentPassword the current password of the user
 * @param newPassword the new password of the user
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ChangePasswordResource(
    @NotBlank(message = "current password is required") String currentPassword,
    @NotBlank(message = "new password is required")
        @Size(min = 8, max = 72, message = "new password must be between 8 and 72 characters")
        String newPassword) {}
