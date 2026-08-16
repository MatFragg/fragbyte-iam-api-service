package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.exceptions.RoleNotFoundException;
import com.fragbyte.iam.domain.model.commands.ProvisionUserCommand;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;
import com.fragbyte.iam.interfaces.rest.resources.ProvisionUserResource;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Assembler that translates {@link ProvisionUserResource} into {@link ProvisionUserCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class ProvisionUserCommandFromResourceAssembler {

  /**
   * Converts the incoming provision user resource to an application command.
   *
   * @param resource the provision user payload from the REST API
   * @return the provision user command consumed by the application layer
   */
  public static ProvisionUserCommand toCommandFrom(ProvisionUserResource resource) {
    Set<AccessRoles> roles =
        resource.roles().stream()
            .map(ProvisionUserCommandFromResourceAssembler::toAccessRole)
            .collect(Collectors.toSet());
    return new ProvisionUserCommand(
        new Email(resource.email()), new RawPassword(resource.password()), roles);
  }

  private static AccessRoles toAccessRole(String roleName) {
    try {
      return AccessRoles.valueOf(roleName);
    } catch (IllegalArgumentException e) {
      throw new RoleNotFoundException(roleName);
    }
  }
}
