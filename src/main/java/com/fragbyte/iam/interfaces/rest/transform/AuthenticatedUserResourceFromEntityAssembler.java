package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembler that translates IAM authentication results into {@link AuthenticatedUserResource}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class AuthenticatedUserResourceFromEntityAssembler {
  /**
   * Creates a resource from the authenticated {@link User} aggregate and issued bearer token.
   *
   * @param user authenticated user aggregate
   * @param token generated bearer token
   * @return resource used by the authentication endpoint response
   */
  public static AuthenticatedUserResource toResourceFrom(User user, String token) {
    return new AuthenticatedUserResource(user.getUserId().value(), user.getEmail().email(), token);
  }
}
