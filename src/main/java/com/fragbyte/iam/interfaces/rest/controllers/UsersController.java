package com.fragbyte.iam.interfaces.rest.controllers;

import com.fragbyte.iam.domain.exceptions.RoleNotFoundException;
import com.fragbyte.iam.domain.model.commands.AssignAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.ChangeEmailCommand;
import com.fragbyte.iam.domain.model.commands.ChangePasswordCommand;
import com.fragbyte.iam.domain.model.commands.DisableUserCommand;
import com.fragbyte.iam.domain.model.commands.LockUserCommand;
import com.fragbyte.iam.domain.model.commands.RemoveAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.UnlockUserCommand;
import com.fragbyte.iam.domain.model.commands.VerifyEmailCommand;
import com.fragbyte.iam.domain.model.queries.GetAllUsersQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.domain.services.UserCommandService;
import com.fragbyte.iam.domain.services.UserQueryService;
import com.fragbyte.iam.interfaces.rest.resources.AssignAccessRoleResource;
import com.fragbyte.iam.interfaces.rest.resources.ChangeEmailResource;
import com.fragbyte.iam.interfaces.rest.resources.ChangePasswordResource;
import com.fragbyte.iam.interfaces.rest.resources.ProvisionUserResource;
import com.fragbyte.iam.interfaces.rest.resources.UserResource;
import com.fragbyte.iam.interfaces.rest.transform.AssignAccessRoleCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.ChangeEmailCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.ChangePasswordCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.ProvisionUserCommandFromResourceAssembler;
import com.fragbyte.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.fragbyte.shared.domain.model.valueobjects.Paged;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * UsersController.
 *
 * <p>This controller is responsible for handling user requests. It exposes the following
 * endpoints:
 *
 * <ul>
 *   <li>GET /api/v1/users
 *   <li>GET /api/v1/users/{userId}
 *   <li>POST /api/v1/users
 *   <li>PATCH /api/v1/users/{userId}/email
 *   <li>PATCH /api/v1/users/{userId}/password
 *   <li>POST /api/v1/users/{userId}/verify
 *   <li>POST /api/v1/users/{userId}/lock
 *   <li>POST /api/v1/users/{userId}/unlock
 *   <li>POST /api/v1/users/{userId}/disable
 *   <li>POST /api/v1/users/{userId}/roles
 *   <li>DELETE /api/v1/users/{userId}/roles/{role}
 * </ul>
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Available User Endpoints")
public class UsersController {
  private final UserQueryService userQueryService;
  private final UserCommandService userCommandService;

  /**
   * Users controller constructor.
   *
   * @param userQueryService the user query service from application layer {@link UserQueryService}
   * @param userCommandService the user command service from application layer {@link
   *     UserCommandService}
   */
  public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
    this.userQueryService = userQueryService;
    this.userCommandService = userCommandService;
  }

  /**
   * Handles the get users request.
   *
   * @param page the zero-based page number
   * @param size the page size
   * @return a paged collection of user resources
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
  public ResponseEntity<ApiResponseResource<Paged<UserResource>>> getAllUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    var getAllUsersQuery = new GetAllUsersQuery(page, size);
    var users = userQueryService.handle(getAllUsersQuery);
    var userResources =
        users.content().stream().map(UserResourceFromEntityAssembler::toResourceFrom).toList();
    var pagedResources =
        Paged.of(userResources, users.totalElements(), users.page(), users.size());
    return ResponseEntity.ok(
        ApiResponseResource.success(
            HttpStatus.OK.value(), "Users retrieved successfully", pagedResources));
  }

  /**
   * Handles the get user by id request.
   *
   * @param userId the user id
   * @return the user resource with the given id
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

  /**
   * Handles the provision user request.
   *
   * @param resource the provision user request body
   * @return the created user resource
   */
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Provision a user",
      description = "Create a user on behalf of the platform. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "User provisioned successfully."),
        @ApiResponse(responseCode = "400", description = "Bad Request."),
        @ApiResponse(responseCode = "404", description = "Role not found."),
        @ApiResponse(responseCode = "409", description = "Email already registered.")
      })
  public ResponseEntity<ApiResponseResource<UserResource>> provisionUser(
      @Valid @RequestBody ProvisionUserResource resource) {
    var command = ProvisionUserCommandFromResourceAssembler.toCommandFrom(resource);
    var user = userCommandService.handle(command);
    var userResource = UserResourceFromEntityAssembler.toResourceFrom(user);
    return new ResponseEntity<>(
        ApiResponseResource.success(
            HttpStatus.CREATED.value(), "User provisioned successfully", userResource),
        HttpStatus.CREATED);
  }

  /**
   * Handles the change email request.
   *
   * @param userId the user id
   * @param resource the change email request body
   * @return the response envelope
   */
  @PatchMapping(value = "/{userId}/email")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #userId == authentication.principal.userId")
  @Operation(
      summary = "Change email",
      description = "Change the email of the user. Allowed: ADMIN or the user themselves.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Email changed successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "409", description = "Email already registered.")
      })
  public ResponseEntity<ApiResponseResource<Void>> changeEmail(
      @PathVariable String userId, @Valid @RequestBody ChangeEmailResource resource) {
    var command = ChangeEmailCommandFromResourceAssembler.toCommandFrom(new UserId(userId), resource);
    userCommandService.handle(command);
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "Email changed successfully", null));
  }

  /**
   * Handles the change password request.
   *
   * @param userId the user id
   * @param resource the change password request body
   * @return the response envelope
   */
  @PatchMapping(value = "/{userId}/password")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #userId == authentication.principal.userId")
  @Operation(
      summary = "Change password",
      description = "Change the password of the user. Allowed: ADMIN or the user themselves.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Password changed successfully."),
        @ApiResponse(responseCode = "401", description = "Current password is invalid."),
        @ApiResponse(responseCode = "404", description = "User not found.")
      })
  public ResponseEntity<ApiResponseResource<Void>> changePassword(
      @PathVariable String userId, @Valid @RequestBody ChangePasswordResource resource) {
    var command =
        ChangePasswordCommandFromResourceAssembler.toCommandFrom(new UserId(userId), resource);
    userCommandService.handle(command);
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "Password changed successfully", null));
  }

  /**
   * Handles the verify email request.
   *
   * @param userId the user id
   * @return the response envelope
   */
  @PostMapping(value = "/{userId}/verify")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN') or #userId == authentication.principal.userId")
  @Operation(
      summary = "Verify email",
      description = "Verify the email of the user. Allowed: ADMIN or the user themselves.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Email verified successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "409", description = "Illegal state transition.")
      })
  public ResponseEntity<ApiResponseResource<Void>> verifyEmail(@PathVariable String userId) {
    userCommandService.handle(new VerifyEmailCommand(new UserId(userId)));
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "Email verified successfully", null));
  }

  /**
   * Handles the lock user request.
   *
   * @param userId the user id
   * @return the response envelope
   */
  @PostMapping(value = "/{userId}/lock")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Lock user",
      description = "Lock the account of the user. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User locked successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "409", description = "Illegal state transition.")
      })
  public ResponseEntity<ApiResponseResource<Void>> lockUser(@PathVariable String userId) {
    userCommandService.handle(new LockUserCommand(new UserId(userId)));
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "User locked successfully", null));
  }

  /**
   * Handles the unlock user request.
   *
   * @param userId the user id
   * @return the response envelope
   */
  @PostMapping(value = "/{userId}/unlock")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Unlock user",
      description = "Unlock the account of the user. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User unlocked successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "409", description = "Illegal state transition.")
      })
  public ResponseEntity<ApiResponseResource<Void>> unlockUser(@PathVariable String userId) {
    userCommandService.handle(new UnlockUserCommand(new UserId(userId)));
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "User unlocked successfully", null));
  }

  /**
   * Handles the disable user request.
   *
   * @param userId the user id
   * @return the response envelope
   */
  @PostMapping(value = "/{userId}/disable")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Disable user",
      description = "Disable the account of the user. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User disabled successfully."),
        @ApiResponse(responseCode = "404", description = "User not found.")
      })
  public ResponseEntity<ApiResponseResource<Void>> disableUser(@PathVariable String userId) {
    userCommandService.handle(new DisableUserCommand(new UserId(userId)));
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "User disabled successfully", null));
  }

  /**
   * Handles the assign access role request.
   *
   * @param userId the user id
   * @param resource the assign access role request body
   * @return the response envelope
   */
  @PostMapping(value = "/{userId}/roles")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Assign access role",
      description = "Grant a platform access role to the user. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Role assigned successfully."),
        @ApiResponse(responseCode = "404", description = "User or role not found.")
      })
  public ResponseEntity<ApiResponseResource<Void>> assignAccessRole(
      @PathVariable String userId, @Valid @RequestBody AssignAccessRoleResource resource) {
    var command =
        AssignAccessRoleCommandFromResourceAssembler.toCommandFrom(new UserId(userId), resource);
    userCommandService.handle(command);
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "Role assigned successfully", null));
  }

  /**
   * Handles the remove access role request.
   *
   * @param userId the user id
   * @param role the role name to revoke
   * @return the response envelope
   */
  @DeleteMapping(value = "/{userId}/roles/{role}")
  @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
  @Operation(
      summary = "Remove access role",
      description = "Revoke a platform access role from the user. Allowed: ADMIN, SUPERADMIN.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Role removed successfully."),
        @ApiResponse(responseCode = "404", description = "User or role not found."),
        @ApiResponse(responseCode = "409", description = "Illegal state transition.")
      })
  public ResponseEntity<ApiResponseResource<Void>> removeAccessRole(
      @PathVariable String userId, @PathVariable String role) {
    var command = new RemoveAccessRoleCommand(new UserId(userId), toAccessRole(role));
    userCommandService.handle(command);
    return ResponseEntity.ok(
        ApiResponseResource.success(HttpStatus.OK.value(), "Role removed successfully", null));
  }

  private AccessRoles toAccessRole(String roleName) {
    try {
      return AccessRoles.valueOf(roleName);
    } catch (IllegalArgumentException e) {
      throw new RoleNotFoundException(roleName);
    }
  }
}
