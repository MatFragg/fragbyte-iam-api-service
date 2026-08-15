package com.hampcoders.glottia.platform.api.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Handles unauthorized requests intercepted by Spring Security.
 *
 * <p>Sends an HTTP 401 (Unauthorized) response whenever authentication is required but the request
 * cannot be authenticated.
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);

  /**
   * Commences the authentication process for an unauthorized request.
   *
   * <p>Logs the authentication failure and returns an HTTP 401 response.
   *
   * @param request the request that triggered the authentication failure
   * @param response the response sent to the client
   * @param authenticationException the authentication failure
   * @throws IOException if the error response cannot be written
   * @throws ServletException if request processing fails
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authenticationException)
      throws IOException, ServletException {
    LOGGER.error("Unauthorized request: {}", authenticationException.getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized request detected");
  }
}
