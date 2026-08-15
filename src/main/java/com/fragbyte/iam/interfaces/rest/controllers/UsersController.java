package com.fragbyte.iam.interfaces.rest.controllers;

import com.fragbyte.iam.domain.model.queries.GetAllUsersQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.domain.services.UserQueryService;
import com.fragbyte.iam.interfaces.rest.resources.UserResource;
import com.fragbyte.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UsersController.
 *
 * <p>This controller is responsible for handling user requests. It exposes three endpoints:
 *
 * <ul>
 *   <li>GET /api/v1/users
 *   <li>GET /api/v1/users/{userId}
 * </ul>
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {
  private final UserQueryService userQueryService;

  /**
   * Users controller constructor.
   *
   * @param userQueryService the user query service from application layer {@link UserQueryService}
   */
  public UsersController(UserQueryService userQueryService) {
    this.userQueryService = userQueryService;
  }

  /**
   * Handles the get users request.
   *
   * @return a list of user resources
   */
  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(summary = "Get all users", description = "Get all the users available in the system.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden.")
      })
  public ResponseEntity<ApiResponseResource<List<UserResource>>> getAllUsers() {
    var getAllUsersQuery = new GetAllUsersQuery();
    var users = userQueryService.handle(getAllUsersQuery);
    var userResources =
        users.stream().map(UserResourceFromEntityAssembler::toResourceFrom).toList();
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "Users retrieved successfully", userResources));
  }

  /**
   * Handles the get user by id request.
   *
   * @param userId the user id
   * @return the user resource with the given id
   * @throws RuntimeException if the user is not found
   * @see UserResource
   */
  @GetMapping(value = "/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #userId == authentication.principal.userId")
  @Operation(
      summary = "Get user by id",
      description = "Get the user with the given id. Allowed: ADMIN or the user themselves.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "401", description = "Unauthorized."),
        @ApiResponse(responseCode = "403", description = "Forbidden.")
      })
  public ResponseEntity<ApiResponseResource<UserResource>> getUserById(
      @PathVariable String userId) {
    var getUserByIdQuery = new GetUserByIdQuery(new UserId(userId));
    var user = userQueryService.handle(getUserByIdQuery);
    if (user.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(ApiResponseResource.error(HttpStatus.NOT_FOUND.value(), "User not found"));
    }
    var userResource = UserResourceFromEntityAssembler.toResourceFrom(user.get());
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "User retrieved successfully", userResource));
  }
}
