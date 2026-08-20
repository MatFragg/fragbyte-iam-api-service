package com.fragbyte.iam.infrastructure.externalidentity.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.fragbyte.iam.application.internal.outboundservices.externalidentity.ExternalIdentityVerifier;
import com.fragbyte.iam.application.internal.outboundservices.externalidentity.VerifiedExternalIdentity;
import com.fragbyte.iam.domain.exceptions.ProviderNotConfiguredException;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

  private final GoogleIdTokenVerifier verifier;

  /**
   * Google external identity verifier constructor.
   *
   * @param googleClientId the Google OAuth client ID
   */
  public GoogleExternalIdentityVerifierImpl(
      @Value("${iam.providers.google.client-id:}") String googleClientId) {
    if (googleClientId == null || googleClientId.isBlank()) {
      throw new IllegalArgumentException(
          "iam.providers.google.client-id must be configured for Google authentication");
    }
    this.verifier =
        new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(googleClientId))
            .build();
  }

  /** {@inheritDoc} */
  @Override
  public VerifiedExternalIdentity verifyToken(AuthProvider provider, String token) {
    if (provider != AuthProvider.GOOGLE) {
      throw new ProviderNotConfiguredException(provider);
    }
    GoogleIdToken idToken;
    try {
      idToken = verifier.verify(token);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to verify Google token: " + e.getMessage(), e);
    }
    if (idToken == null) {
      throw new IllegalArgumentException("Invalid or expired Google ID token");
    }
    GoogleIdToken.Payload payload = idToken.getPayload();
    return new VerifiedExternalIdentity(
        AuthProvider.GOOGLE, payload.getSubject(), payload.getEmail());
  }
}
