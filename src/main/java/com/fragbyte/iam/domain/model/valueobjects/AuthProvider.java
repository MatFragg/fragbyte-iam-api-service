package com.fragbyte.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

/**
 * Authentication provider.
 *
 * <p>Identifies the source of identity verification for a user. {@code LOCAL} represents
 * email/password authentication handled within the platform. External providers (e.g. {@code
 * GOOGLE}) represent federated identity sources.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Embeddable
public enum AuthProvider {
  LOCAL,
  GOOGLE;
}
