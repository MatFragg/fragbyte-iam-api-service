package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.SignInWithProviderCommand;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.interfaces.rest.resources.SignInWithProviderResource;

/**
 * Assembler that translates {@link SignInWithProviderResource} into {@link SignInWithProviderCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
public class SignInWithProviderCommandFromResourceAssembler {

  /**
   * Converts the incoming sign-in with provider resource to an application command.
   *
   * @param provider the external authentication provider
   * @param resource sign-in with provider payload from REST API
   * @return sign-in with provider command consumed by the application layer
   */
  public static SignInWithProviderCommand toCommandFrom(
      AuthProvider provider, SignInWithProviderResource resource) {
    return new SignInWithProviderCommand(provider, resource.token());
  }
}
