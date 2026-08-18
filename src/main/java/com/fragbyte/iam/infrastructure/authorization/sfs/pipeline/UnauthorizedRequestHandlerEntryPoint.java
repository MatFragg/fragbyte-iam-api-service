package com.fragbyte.iam.infrastructure.authorization.sfs.pipeline;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Handles unauthorized requests intercepted by Spring Security.
 *
 * <p>Writes a standard {@link ApiResponseResource} 401 envelope whenever authentication is
 * required but the request cannot be authenticated.
 */
@Component
public class UnauthorizedRequestHandlerEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(UnauthorizedRequestHandlerEntryPoint.class);
  private static final String UNAUTHORIZED_MESSAGE =
      "Full authentication is required to access this resource";

  private final ObjectMapper objectMapper =
    JsonMapper.builder()
      .addModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .build();

  /**
   * Commences the authentication process for an unauthorized request.
   *
   * <p>Logs the authentication failure and writes a 401 envelope response.
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
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    var envelope = ApiResponseResource.error(HttpStatus.UNAUTHORIZED.value(), UNAUTHORIZED_MESSAGE);
    objectMapper.writeValue(response.getWriter(), envelope);
  }
}
