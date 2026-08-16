package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.SignInCommand;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;
import com.fragbyte.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembler that translates {@link SignInResource} into {@link SignInCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class SignInCommandFromResourceAssembler {
  /**
   * Converts the incoming sign-in resource to an application command.
   *
   * @param signInResource sign-in payload from REST API
   * @return sign-in command consumed by the application layer
   */
  public static SignInCommand toCommandFrom(SignInResource signInResource) {
    return new SignInCommand(
        new Email(signInResource.email()), new RawPassword(signInResource.password()));
  }
}
