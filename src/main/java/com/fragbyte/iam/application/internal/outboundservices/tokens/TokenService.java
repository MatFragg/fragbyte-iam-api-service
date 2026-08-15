package com.fragbyte.iam.application.internal.outboundservices.tokens;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;

/** TokenService interface. This interface is used to generate and validate tokens
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface TokenService {

  /**
   * Generate a token with full user claims.
   *
   * @param username the username
   * @param userId the user id
   * @param accessRole the platform access role (USER / ADMIN / SUPERADMIN / SUPPORT)
   * @return String the token
   */
  String generateToken(
      String username,
      String userId,
      AccessRoles accessRole);

  /**
   * Extract the username from a token.
   *
   * @param token the token
   * @return String the username
   */
  String getUsernameFromToken(String token);

  /**
   * Validate a token.
   *
   * @param token the token
   * @return boolean true if the token is valid, false otherwise
   */
  boolean validateToken(String token);

  /**
   * Extract the user id from a token.
   *
   * <p>Tolerates expired tokens so a JWT that has outlived its access window can still be used to
   * obtain a fresh one.
   *
   * @param token the token
   * @return the user identifier extracted from the token
   */
  String getUserIdFromToken(String token);
}
