package com.fragbyte.iam.interfaces.rest.controllers;

import com.fragbyte.iam.domain.model.commands.SignInWithProviderCommand;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.services.UserCommandService;
import com.fragbyte.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.fragbyte.iam.interfaces.rest.resources.RefreshedTokenResource;
import com.fragbyte.iam.interfaces.rest.resources.SignInResource;
import com.fragbyte.iam.interfaces.rest.resources.SignInWithProviderResource;
import com.fragbyte.iam.interfaces.rest.resources.SignUpResource;
import com.fragbyte.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.SignInWithProviderCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.fragbyte.iam.interfaces.rest.transform.RefreshTokenCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.RefreshedTokenResourceFromTokenAssembler;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController.
 *
 * <p>This controller is responsible for handling authentication requests. It exposes four
 * endpoints:
 *
 * <ul>
 *   <li>POST /api/v1/authentication/sign-in
 *   <li>POST /api/v1/authentication/sign-in/google
 *   <li>POST /api/v1/authentication/sign-up
 *   <li>POST /api/v1/authentication/refresh-token
 * </ul>
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {

  private final UserCommandService userCommandService;

  /**
   * Authentication controller constructor.
   *
   * @param userCommandService the user command service from application layer {@link
   *     UserCommandService}
   */
  public AuthenticationController(UserCommandService userCommandService) {
    this.userCommandService = userCommandService;
  }

  /**
   * Handles the sign-in request.
   *
   * @param signInResource the sign-in request body.
   * @return the authenticated user resource.
   */
  @PostMapping("/sign-in")
  @RateLimiter(name = "signIn")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found")
      })
  public ResponseEntity<ApiResponseResource<AuthenticatedUserResource>> signIn(
      @Valid @RequestBody SignInResource signInResource) {
    var signInCommand = SignInCommandFromResourceAssembler.toCommandFrom(signInResource);
    var authenticatedUser = userCommandService.handle(signInCommand);
    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponseResource.error(HttpStatus.NOT_FOUND.value(), "User not found"));
    }

    var authenticatedUserResource =
        AuthenticatedUserResourceFromEntityAssembler.toResourceFrom(
            authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "Sign in successful", authenticatedUserResource));
  }

  /**
   * Handles the sign-in with Google request.
   *
   * @param resource the sign-in with Google request body.
   * @return the authenticated user resource.
   */
  @PostMapping("/sign-in/google")
  @RateLimiter(name = "signIn")
  @Operation(
      summary = "Sign in with Google",
      description = "Authenticate a user via a Google ID token.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found")
      })
  public ResponseEntity<ApiResponseResource<AuthenticatedUserResource>> signInWithGoogle(
      @Valid @RequestBody SignInWithProviderResource resource) {
    var command =
        SignInWithProviderCommandFromResourceAssembler.toCommandFrom(
            AuthProvider.GOOGLE, resource);
    var authenticatedUser = userCommandService.handle(command);
    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponseResource.error(HttpStatus.NOT_FOUND.value(), "User not found"));
    }
    var authenticatedUserResource =
        AuthenticatedUserResourceFromEntityAssembler.toResourceFrom(
            authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "Sign in successful", authenticatedUserResource));
  }

  /**
   * Handles the sign-up request.
   *
   * @param signUpResource the sign-up request body.
   * @return the created user resource.
   */
  @PostMapping("/sign-up")
  @RateLimiter(name = "signUp")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
      })
  public ResponseEntity<ApiResponseResource<AuthenticatedUserResource>> signUp(
      @Valid @RequestBody SignUpResource signUpResource) {
    var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFrom(signUpResource);
    var authenticatedUser = userCommandService.handle(signUpCommand);
    if (authenticatedUser.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(ApiResponseResource.error(HttpStatus.BAD_REQUEST.value(), "Bad request"));
    }

    var authenticatedUserResource =
        AuthenticatedUserResourceFromEntityAssembler.toResourceFrom(
            authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());
    return new ResponseEntity<>(
        ApiResponseResource.success(
            HttpStatus.CREATED.value(), "User registered successfully", authenticatedUserResource),
        HttpStatus.CREATED);
  }

  /**
   * Handles the refresh token request.
   *
   * <p>Re-issues a fresh JWT for the user identified by the presented JWT. The JWT is the one
   * issued at sign-in/sign-up and may already be expired.
   *
   * @param request the current HTTP request carrying the JWT in the Authorization header
   * @return the refreshed jwt.
   */
  @PostMapping("/refresh-token")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
      })
  public ResponseEntity<ApiResponseResource<RefreshedTokenResource>> refreshToken(
      HttpServletRequest request) {
    var command =
        RefreshTokenCommandFromResourceAssembler.toCommandFrom(
            request.getHeader("Authorization"));
    if (command == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(
              ApiResponseResource.error(
                  HttpStatus.UNAUTHORIZED.value(), "A bearer token is required"));
    }
    var result = userCommandService.handle(command);
    if (result.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(ApiResponseResource.error(HttpStatus.UNAUTHORIZED.value(), "Unauthorized"));
    }
    var user = result.get().getLeft();
    var refreshedToken = result.get().getRight();
    var resource = RefreshedTokenResourceFromTokenAssembler.toResourceFrom(user, refreshedToken);
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "Token refreshed successfully", resource));
  }
}
