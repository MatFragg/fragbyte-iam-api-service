package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;

/**
 * Sign in with provider command.
 *
 * <p>This class represents the command to authenticate a user via an external identity provider.
 *
 * @param provider the external authentication provider.
 * @param providerToken the identity token from the provider (e.g. Google ID token).
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record SignInWithProviderCommand(AuthProvider provider, String providerToken) {
  public SignInWithProviderCommand {
    if (provider == null) {
      throw new IllegalArgumentException("Provider cannot be null");
    }
    if (providerToken == null || providerToken.isBlank()) {
      throw new IllegalArgumentException("Provider token cannot be null or blank");
    }
  }
}
