package com.fragbyte.iam.domain.exceptions;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;

/**
 * Federated identity already linked exception.
 *
 * <p>Thrown when attempting to link a federated identity that is already associated with another
 * user account.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public class FederatedIdentityAlreadyLinkedException extends RuntimeException {

  /**
   * The federated identity already linked exception constructor.
   *
   * @param provider the provider
   * @param providerSubject the provider subject
   */
  public FederatedIdentityAlreadyLinkedException(AuthProvider provider, String providerSubject) {
    super(
        "Federated identity "
            + provider.name()
            + ":"
            + providerSubject
            + " is already linked to another account");
  }
}
