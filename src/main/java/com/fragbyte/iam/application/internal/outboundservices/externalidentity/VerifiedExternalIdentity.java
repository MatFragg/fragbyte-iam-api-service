package com.fragbyte.iam.application.internal.outboundservices.externalidentity;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;

/**
 * Verified external identity.
 *
 * <p>Represents the verified identity claims extracted from an external provider's token. The domain
 * uses this to link or look up users without knowing the provider's token format.
 *
 * @param provider the external authentication provider
 * @param providerSubject the unique subject identifier from the provider
 * @param providerEmail the email address from the provider (may be null)
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record VerifiedExternalIdentity(
    AuthProvider provider, String providerSubject, String providerEmail) {}
