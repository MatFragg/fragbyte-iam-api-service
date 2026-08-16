package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.ChangeEmailCommand;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.interfaces.rest.resources.ChangeEmailResource;

/**
 * Assembler that translates a {@link ChangeEmailResource} into a {@link ChangeEmailCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class ChangeEmailCommandFromResourceAssembler {

  /**
   * Converts the incoming change email resource to an application command.
   *
   * @param userId the identifier of the target user
   * @param resource the change email payload from the REST API
   * @return the change email command consumed by the application layer
   */
  public static ChangeEmailCommand toCommandFrom(UserId userId, ChangeEmailResource resource) {
    return new ChangeEmailCommand(userId, new Email(resource.email()));
  }
}
