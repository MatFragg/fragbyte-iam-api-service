package com.hampcoders.glottia.platform.api.iam.infrastructure.tokens.jwt.services;

import com.hampcoders.glottia.platform.api.iam.domain.model.valueobjects.AccessRole;
import com.hampcoders.glottia.platform.api.iam.infrastructure.tokens.jwt.BearerTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * This class implements the {@link BearerTokenService} interface. It is used to manage operations
 * related to token generation.
 */
@Service
public class TokenServiceImpl implements BearerTokenService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

  private static final String AUTHORIZATION_PARAMETER_NAME = "Authorization";
  private static final String BEARER_TOKEN_PREFIX = "Bearer ";

  private static final int TOKEN_BEGIN_INDEX = 7;

  @Value("${authorization.jwt.secret}")
  private String secret;

  @Value("${authorization.jwt.expiration.days}")
  private int expirationDays;

  @Override
  public String getUsernameFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
      LOGGER.info("Token is valid");
      return true;
    } catch (SignatureException e) {
      LOGGER.error("Invalid JSON Web Token Signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JSON Web Token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JSON Web Token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JSON Web Token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JSON Web Token claims string is empty: {}", e.getMessage());
    }
    return false;
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
    final Claims claims = extractAllClaims(token);
    return claimsResolvers.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenPresentIn(String authorizationParameter) {
    return StringUtils.hasText(authorizationParameter);
  }

  private boolean isBearerTokenIn(String authorizationParameter) {
    return authorizationParameter.startsWith(BEARER_TOKEN_PREFIX);
  }

  private String extractTokenFrom(String authorizationHeaderParameter) {
    return authorizationHeaderParameter.substring(TOKEN_BEGIN_INDEX);
  }

  private String getAuthorizationParameterFrom(HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION_PARAMETER_NAME);
  }

  @Override
  public String getBearerTokenFrom(HttpServletRequest request) {
    String parameter = getAuthorizationParameterFrom(request);
    if (isTokenPresentIn(parameter) && isBearerTokenIn(parameter)) {
      return extractTokenFrom(parameter);
    }
    return null;
  }

  /** {@inheritDoc} */
  public String getRoleFromToken(String token) {
    return extractAllClaims(token).get("role", String.class);
  }

  @Override
  public String generateToken(
      String username,
      String userId,
      AccessRole accessRole,
      String role,
      String roleSpecificId,
      String profileStatus,
      String profileId) {
    var issuedAt = new Date();
    var expiration = DateUtils.addDays(issuedAt, expirationDays);

    var builder =
        Jwts.builder()
            .subject(username)
            .claim("userId", userId)
            .claim("accessRole", accessRole != null ? accessRole.name() : AccessRole.USER.name())
            .claim("role", role)
            .issuedAt(issuedAt)
            .expiration(expiration);

    if (profileStatus != null && !profileStatus.isBlank()) {
      builder.claim("profileStatus", profileStatus);
    }

    if (profileId != null && !profileId.isBlank()) {
      builder.claim("profileId", profileId);
    }

    if ("LEARNER".equals(role) && roleSpecificId != null && !roleSpecificId.isBlank()) {
      builder.claim("learnerId", roleSpecificId);
    } else if ("PARTNER".equals(role) && roleSpecificId != null && !roleSpecificId.isBlank()) {
      builder.claim("partnerId", roleSpecificId);
    }

    var key = getSigningKey();

    return builder.signWith(key).compact();
  }

  /** {@inheritDoc} */
  public String getUserIdFromToken(String token) {
    return extractAllClaims(token).get("userId", String.class);
  }

  /** {@inheritDoc} */
  public String getLearnerIdStringFromToken(String token) {
    return extractAllClaims(token).get("learnerId", String.class);
  }

  /** {@inheritDoc} */
  public String getPartnerIdStringFromToken(String token) {
    return extractAllClaims(token).get("partnerId", String.class);
  }

  /** {@inheritDoc} */
  public String getProfileIdFromToken(String token) {
    return extractAllClaims(token).get("profileId", String.class);
  }

  /** {@inheritDoc} */
  public String getAccessRoleFromToken(String token) {
    return extractAllClaims(token).get("accessRole", String.class);
  }

  @Override
  public Claims extractAllClaimsFromToken(String token) {
    try {
      return Jwts.parser()
          .verifyWith(getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}
