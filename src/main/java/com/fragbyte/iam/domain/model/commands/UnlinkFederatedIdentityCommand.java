package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Unlink federated identity command.
 *
 * <p>This class represents the command to remove an external identity provider link from a user
 * account.
 *
 * @param userId the identifier of the user.
 * @param provider the external authentication provider to unlink.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public record UnlinkFederatedIdentityCommand(UserId userId, AuthProvider provider) {
  public UnlinkFederatedIdentityCommand {
    if (userId == null) {
      throw new IllegalArgumentException("UserId cannot be null");
    }
    if (provider == null) {
      throw new IllegalArgumentException("Provider cannot be null");
    }
  }
}
