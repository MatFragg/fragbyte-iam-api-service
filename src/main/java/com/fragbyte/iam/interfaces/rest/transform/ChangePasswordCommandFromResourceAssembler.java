package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.ChangePasswordCommand;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.interfaces.rest.resources.ChangePasswordResource;

/**
 * Assembler that translates a {@link ChangePasswordResource} into a {@link ChangePasswordCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class ChangePasswordCommandFromResourceAssembler {

  /**
   * Converts the incoming change password resource to an application command.
   *
   * @param userId the identifier of the target user
   * @param resource the change password payload from the REST API
   * @return the change password command consumed by the application layer
   */
  public static ChangePasswordCommand toCommandFrom(UserId userId, ChangePasswordResource resource) {
    return new ChangePasswordCommand(
        userId,
        new RawPassword(resource.currentPassword()),
        new RawPassword(resource.newPassword()));
  }
}
