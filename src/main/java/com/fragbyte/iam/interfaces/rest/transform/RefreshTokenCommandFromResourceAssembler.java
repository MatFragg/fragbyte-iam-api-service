package com.fragbyte.iam.interfaces.rest.transform;

import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;

/**
 * Assembler that translates an HTTP Authorization header into a {@link RefreshTokenCommand}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class RefreshTokenCommandFromResourceAssembler {

  /**
   * Converts the presented Authorization header to a refresh token command.
   *
   * @param authorizationHeader the HTTP Authorization header value, expected to use the {@code
   *     Bearer} scheme.
   * @return the refresh token command consumed by the application layer, or {@code null} if the
   *     header is missing or does not use the Bearer scheme.
   */
  public static RefreshTokenCommand toCommandFrom(String authorizationHeader) {
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return new RefreshTokenCommand(authorizationHeader.substring(7));
    }
    return null;
  }
}
