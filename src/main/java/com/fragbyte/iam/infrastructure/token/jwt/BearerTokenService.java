package com.hampcoders.glottia.platform.api.iam.infrastructure.tokens.jwt;

import com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.tokens.TokenService;
import com.hampcoders.glottia.platform.api.iam.domain.model.valueobjects.AccessRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

/** Marker interface for the JWT token service. Extends {@link TokenService}. */
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
   * @param role the business role (LEARNER, PARTNER, or UNASSIGNED) — opaque to IAM
   * @param roleSpecificId the learner or partner id — opaque to IAM
   * @param profileStatus the profile status (INCOMPLETE, ACTIVE, DEACTIVATED) — opaque to IAM
   * @param profileId the profile id (pr-uuid) — opaque to IAM
   * @return the generated token
   */
  String generateToken(
      String username,
      String userId,
      AccessRole accessRole,
      String role,
      String roleSpecificId,
      String profileStatus,
      String profileId);

  /**
   * Gets user id from token.
   *
   * @param token the generated token
   * @return the user identifier extracted from the token
   */
  String getUserIdFromToken(String token);

  /**
   * Gets user role from the token.
   *
   * @param token the generated token
   * @return the role extracted from the token
   */
  String getRoleFromToken(String token);

  /**
   * Gets the learner id from the token.
   *
   * @param token the generated token
   * @return the learner id from the token
   */
  String getLearnerIdStringFromToken(String token);

  /**
   * Gets the partner id from the token.
   *
   * @param token the generated token
   * @return the partner id from the token
   */
  String getPartnerIdStringFromToken(String token);

  /**
   * Gets the profile id from the token.
   *
   * @param token the generated token
   * @return the partner id from the token
   */
  String getProfileIdFromToken(String token);

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
