package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.LinkFederatedIdentityCommand;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.interfaces.rest.resources.LinkFederatedIdentityResource;

/**
 * Assembler that translates {@link LinkFederatedIdentityResource} into {@link
 * LinkFederatedIdentityCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public class LinkFederatedIdentityCommandFromResourceAssembler {

  /**
   * Converts the incoming link federated identity resource to an application command.
   *
   * @param userId the identifier of the target user
   * @param provider the external authentication provider
   * @param resource link federated identity payload from REST API
   * @return link federated identity command consumed by the application layer
   */
  public static LinkFederatedIdentityCommand toCommandFrom(
      UserId userId, AuthProvider provider, LinkFederatedIdentityResource resource) {
    return new LinkFederatedIdentityCommand(userId, provider, resource.token());
  }
}
