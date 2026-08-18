package com.fragbyte.iam.domain.exceptions;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;

/**
 * Provider not configured exception.
 *
 * <p>Thrown when attempting to authenticate with an external provider that is not configured in the
 * system.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public class ProviderNotConfiguredException extends RuntimeException {

  /**
   * The provider not configured exception constructor.
   *
   * @param provider the provider that is not configured
   */
  public ProviderNotConfiguredException(AuthProvider provider) {
    super("Provider " + provider.name() + " is not configured");
  }
}
