package com.fragbyte.shared.interfaces.rest.controllers;

import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Renders the standard {@link ApiResponseResource} envelope for the container-level {@code /error}
 * fallback (unmapped paths, unexpected container errors, etc.).
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@RestController
public class ApiErrorController implements ErrorController {

  /**
   * Handles the {@code /error} fallback dispatch.
   *
   * @param request the current HTTP request carrying the error attributes
   * @return the standard error envelope
   */
  @RequestMapping("/error")
  public ApiResponseResource<Void> error(HttpServletRequest request) {
    var statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    int status =
        statusCode instanceof Integer code
            ? code
            : HttpStatus.INTERNAL_SERVER_ERROR.value();
    String message =
        status == HttpStatus.NOT_FOUND.value()
            ? "Resource not found"
            : "An unexpected error occurred. Please try again later.";
    return ApiResponseResource.error(status, message);
  }
}
