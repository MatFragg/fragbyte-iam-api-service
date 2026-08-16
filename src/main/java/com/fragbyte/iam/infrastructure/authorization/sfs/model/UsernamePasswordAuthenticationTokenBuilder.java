package com.fragbyte.iam.infrastructure.authorization.sfs.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

/**
 * Utility class for creating authenticated {@link UsernamePasswordAuthenticationToken} instances.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public class UsernamePasswordAuthenticationTokenBuilder {

  /**
   * Creates an authenticated {@link UsernamePasswordAuthenticationToken} from the provided user
   * details.
   *
   * @param principal the authenticated user
   * @param request the current HTTP request
   * @return the authentication token populated with request details
   */
  public static UsernamePasswordAuthenticationToken build(
    UserDetails principal, HttpServletRequest request) {
    var usernamePasswordAuthenticationToken =
      new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    usernamePasswordAuthenticationToken.setDetails(
      new WebAuthenticationDetailsSource().buildDetails(request));
    return usernamePasswordAuthenticationToken;
  }
}