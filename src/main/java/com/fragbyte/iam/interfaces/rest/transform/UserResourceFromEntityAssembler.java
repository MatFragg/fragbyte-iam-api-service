package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.interfaces.rest.resources.UserResource;

/**
 * Assembler that converts IAM {@link User} aggregates into REST {@link UserResource} objects.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class UserResourceFromEntityAssembler {

  /**
   * Converts a user aggregate to its REST representation.
   *
   * @param user user aggregate
   * @return user resource
   */
  public static UserResource toResourceFrom(User user) {
    return new UserResource(user.getUserId().value(), user.getEmail().email());
  }
}
