package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.SignUpCommand;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;
import com.fragbyte.iam.interfaces.rest.resources.SignUpResource;

/**
 * Assembler that translates {@link SignUpResource} into {@link SignUpCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class SignUpCommandFromResourceAssembler {

  /**
   * Converts the incoming sign-up resource to an application command.
   *
   * @param resource sign-up payload from REST API
   * @return sign-up command consumed by the application layer
   */
  public static SignUpCommand toCommandFrom(SignUpResource resource) {
    return new SignUpCommand(new Email(resource.email()), new RawPassword(resource.password()));
  }
}
