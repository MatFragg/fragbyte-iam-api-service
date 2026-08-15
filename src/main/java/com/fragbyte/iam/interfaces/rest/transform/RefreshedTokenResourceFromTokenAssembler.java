package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.interfaces.rest.resources.RefreshedTokenResource;

/**
 * Assembler that converts IAM refreshed token into {@link RefreshedTokenResource}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class RefreshedTokenResourceFromTokenAssembler {

  /**
   * Creates a resource from the issues bearer token.
   *
   * @param user authenticated user aggregate
   * @param token issues bearer token.
   * @return resource used by the authentication endpoint response
   */
  public static RefreshedTokenResource toResourceFrom(User user, String token) {
    return new RefreshedTokenResource(token, user.getEmail().email());
  }
}
