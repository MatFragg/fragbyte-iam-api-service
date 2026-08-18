package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Sign in with provider resource.
 *
 * <p>This class represents the resource to sign-in via an external identity provider.
 *
 * @param token the identity token from the external provider (e.g. Google ID token)
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record SignInWithProviderResource(
    @NotBlank(message = "token is required") String token) {}
