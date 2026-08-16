package com.fragbyte.iam.infrastructure.authorization.sfs.pipeline;

import com.fragbyte.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.fragbyte.iam.infrastructure.authorization.sfs.model.UsernamePasswordAuthenticationTokenBuilder;
import com.fragbyte.iam.infrastructure.token.jwt.BearerTokenService;
import com.fragbyte.shared.infrastructure.security.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Spring Security filter that authenticates bearer token requests.
 *
 * <p>Extracts the JWT from the {@code Authorization} header, validates it, and populates the {@link
 * SecurityContextHolder} with the authenticated user.
 *
 * <p>Requests targeting public endpoints defined in {@link SecurityConstants#PUBLIC_PATHS} are
 * ignored.
 */
public class BearerAuthorizationRequestFilter extends OncePerRequestFilter {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(BearerAuthorizationRequestFilter.class);
  private final BearerTokenService tokenService;

  /**
   * Creates a new bearer authorization filter.
   *
   * @param tokenService the service used to parse and validate JWTs
   */
  public BearerAuthorizationRequestFilter(BearerTokenService tokenService) {
    this.tokenService = tokenService;
  }

  /**
   * Processes an incoming HTTP request.
   *
   * <p>If a valid bearer token is present, the authenticated user is stored in the current {@link
   * SecurityContextHolder}.
   *
   * @param request the incoming request
   * @param response the outgoing response
   * @param filterChain the remaining security filter chain
   * @throws ServletException if the request cannot be processed
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = tokenService.getBearerTokenFrom(request);

      if (token == null) {
        LOGGER.info("No bearer token provided; continuing unauthenticated");
        filterChain.doFilter(request, response);
        return;
      }

      if (tokenService.validateToken(token)) {
        var claims = tokenService.extractAllClaimsFromToken(token);
        setAuthenticationFromClaims(claims, request);
      } else {
        LOGGER.info("Token is not valid");
      }

    } catch (Exception e) {
      LOGGER.error("Cannot set user authentication: {}", e.getMessage());
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Creates and stores the authenticated user from JWT claims.
   *
   * @param claims the validated JWT claims
   * @param request the current HTTP request
   */
  private void setAuthenticationFromClaims(Claims claims, HttpServletRequest request) {
    String username = claims.getSubject();
    String userId = claims.get("userId", String.class);
    String accessRoleClaim = claims.get("accessRole", String.class);

    String authority =
        "ROLE_"
            + (accessRoleClaim != null && !accessRoleClaim.isBlank() ? accessRoleClaim : "USER");

    var userDetails =
        new UserDetailsImpl(
            userId,
            username,
            null,
            List.of(new SimpleGrantedAuthority(authority)));
    SecurityContextHolder.getContext()
        .setAuthentication(UsernamePasswordAuthenticationTokenBuilder.build(userDetails, request));
  }

  /**
   * Determines whether authentication should be skipped.
   *
   * <p>Requests targeting public endpoints are excluded from JWT authentication.
   *
   * @param request the current HTTP request
   * @return {@code true} if the filter should be skipped; otherwise {@code false}
   */
  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    String path = request.getServletPath();
    for (String publicPath : SecurityConstants.PUBLIC_PATHS) {
      String prefix =
          publicPath.endsWith("/**")
              ? publicPath.substring(0, publicPath.length() - 3)
              : publicPath;
      if (path.equals(publicPath) || path.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }
}
