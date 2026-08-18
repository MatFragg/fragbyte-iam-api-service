package com.fragbyte.iam.application.internal.outboundservices.externalidentity;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;

/**
 * External identity verifier port.
 *
 * <p>Verifies identity tokens issued by external authentication providers (e.g. Google) and
 * extracts verified identity claims. The domain never sees the provider's token format — this port
 * translates it into a {@link VerifiedExternalIdentity}.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public interface ExternalIdentityVerifier {

  /**
   * Verifies an identity token from the given provider and extracts the verified identity claims.
   *
   * @param provider the external authentication provider
   * @param token the identity token to verify (e.g. a Google ID token)
   * @return the verified identity claims
   * @throws com.fragbyte.iam.domain.exceptions.ProviderNotConfiguredException if the provider is
   *     not configured
   * @throws IllegalArgumentException if the token is invalid or cannot be verified
   */
  VerifiedExternalIdentity verifyToken(AuthProvider provider, String token);
}
