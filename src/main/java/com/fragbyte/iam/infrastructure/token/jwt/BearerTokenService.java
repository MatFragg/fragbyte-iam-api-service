package com.fragbyte.iam.infrastructure.token.jwt;

import com.fragbyte.iam.application.internal.outboundservices.tokens.TokenService;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

/** Marker interface for the JWT token service. Extends {@link TokenService}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface BearerTokenService extends TokenService {

  /**
   * Generates a token from request.
   *
   * @param token the token
   * @return the token
   */
  String getBearerTokenFrom(HttpServletRequest token);

  /**
   * Generates a token from certain parameters.
   *
   * @param username the username
   * @param userId the user id
   * @param accessRole the platform access role (USER / ADMIN / SUPERADMIN / SUPPORT)
   * @return the generated token
   */
  String generateToken(
      String username,
      String userId,
      AccessRoles accessRole);

  /**
   * Gets user id from token.
   *
   * @param token the generated token
   * @return the user identifier extracted from the token
   */
  String getUserIdFromToken(String token);

  /**
   * Gets access role from the token.
   *
   * @param token the generated token
   * @return the access from of the user
   */
  String getAccessRoleFromToken(String token);

  /**
   * Extracts all the claims from the token.
   *
   * @param token the generated token
   * @return the claims from the token
   */
  Claims extractAllClaimsFromToken(String token);
}
