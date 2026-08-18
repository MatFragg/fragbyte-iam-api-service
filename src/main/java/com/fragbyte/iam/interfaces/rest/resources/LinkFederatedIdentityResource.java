package com.fragbyte.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Link federated identity resource.
 *
 * <p>This class represents the resource to link an external identity provider to an existing user.
 *
 * @param token the identity token from the external provider
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record LinkFederatedIdentityResource(
    @NotBlank(message = "token is required") String token) {}
