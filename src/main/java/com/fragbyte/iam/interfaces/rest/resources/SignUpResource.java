package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Sign up resource.
 *
 * <p>This class represents the resource to sign-up within the platform
 *
 * @param email the email of the user
 * @param password the password of the user
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record SignUpResource(
  @NotBlank(message = "email is required") @Email(message = "email must be valid") String email,
  @NotBlank(message = "password is required")
  @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
  String password) {}
