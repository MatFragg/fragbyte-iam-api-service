package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.exceptions.RoleNotFoundException;
import com.fragbyte.iam.domain.model.commands.AssignAccessRoleCommand;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.interfaces.rest.resources.AssignAccessRoleResource;

/**
 * Assembler that translates an {@link AssignAccessRoleResource} into an {@link
 * AssignAccessRoleCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class AssignAccessRoleCommandFromResourceAssembler {

  /**
   * Converts the incoming assign access role resource to an application command.
   *
   * @param userId the identifier of the target user
   * @param resource the assign access role payload from the REST API
   * @return the assign access role command consumed by the application layer
   */
  public static AssignAccessRoleCommand toCommandFrom(
      UserId userId, AssignAccessRoleResource resource) {
    try {
      return new AssignAccessRoleCommand(userId, AccessRoles.valueOf(resource.role()));
    } catch (IllegalArgumentException e) {
      throw new RoleNotFoundException(resource.role());
    }
  }
}
