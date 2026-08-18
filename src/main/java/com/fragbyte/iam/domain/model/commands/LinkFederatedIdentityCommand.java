package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Link federated identity command.
 *
 * <p>This class represents the command to link an external identity provider account to an existing
 * user.
 *
 * @param userId the identifier of the user.
 * @param provider the external authentication provider.
 * @param providerToken the identity token from the provider.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record LinkFederatedIdentityCommand(UserId userId, AuthProvider provider, String providerToken) {
  public LinkFederatedIdentityCommand {
    if (userId == null) {
      throw new IllegalArgumentException("UserId cannot be null");
    }
    if (provider == null) {
      throw new IllegalArgumentException("Provider cannot be null");
    }
    if (providerToken == null || providerToken.isBlank()) {
      throw new IllegalArgumentException("Provider token cannot be null or blank");
    }
  }
}
