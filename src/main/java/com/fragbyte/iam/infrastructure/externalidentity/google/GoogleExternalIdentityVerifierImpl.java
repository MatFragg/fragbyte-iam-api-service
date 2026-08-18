package com.fragbyte.iam.infrastructure.externalidentity.google;

import com.fragbyte.iam.application.internal.outboundservices.externalidentity.ExternalIdentityVerifier;
import com.fragbyte.iam.application.internal.outboundservices.externalidentity.VerifiedExternalIdentity;
import com.fragbyte.iam.domain.exceptions.ProviderNotConfiguredException;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Google external identity verifier.
 *
 * <p>Verifies Google ID tokens and extracts verified identity claims. Uses Google's Token
 * Verifier library for token verification.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Service
public class GoogleExternalIdentityVerifierImpl implements ExternalIdentityVerifier {

  private final String googleClientId;

  /**
   * Google external identity verifier constructor.
   *
   * @param googleClientId the Google OAuth client ID
   */
  public GoogleExternalIdentityVerifierImpl(
      @Value("${iam.providers.google.client-id:}") String googleClientId) {
    this.googleClientId = googleClientId;
  }

  /** {@inheritDoc} */
  @Override
  public VerifiedExternalIdentity verifyToken(AuthProvider provider, String token) {
    if (provider != AuthProvider.GOOGLE) {
      throw new ProviderNotConfiguredException(provider);
    }
    if (googleClientId == null || googleClientId.isBlank()) {
      throw new ProviderNotConfiguredException(provider);
    }
    // TODO: implement actual Google token verification using Google's Token Verifier library
    // For now, throw as this is a stub
    throw new UnsupportedOperationException(
        "Google token verification not yet implemented. Configure iam.providers.google.client-id.");
  }
}
