package com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.tokens;

import com.hampcoders.glottia.platform.api.iam.domain.model.valueobjects.AccessRole;

/** TokenService interface. This interface is used to generate and validate tokens */
public interface TokenService {

  /**
   * Generate a token with full user claims.
   *
   * @param username the username
   * @param userId the user id
   * @param accessRole the platform access role (USER / ADMIN / SUPERADMIN / SUPPORT)
   * @param role the business role (LEARNER, PARTNER, or UNASSIGNED) — opaque to IAM
   * @param roleSpecificId the learner or partner id — opaque to IAM
   * @param profileStatus the profile status (INCOMPLETE, ACTIVE, DEACTIVATED) — opaque to IAM
   * @param profileId the profile id (pr-uuid) — opaque to IAM
   * @return String the token
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
}
