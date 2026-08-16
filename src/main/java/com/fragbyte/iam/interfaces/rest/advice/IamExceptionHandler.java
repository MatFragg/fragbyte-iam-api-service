package com.fragbyte.iam.interfaces.rest.advice;

import com.fragbyte.iam.domain.exceptions.AccountDisabledException;
import com.fragbyte.iam.domain.exceptions.AccountLockedException;
import com.fragbyte.iam.domain.exceptions.AccountNotVerifiedException;
import com.fragbyte.iam.domain.exceptions.EmailAlreadyExistsException;
import com.fragbyte.iam.domain.exceptions.IllegalAccountStateTransitionException;
import com.fragbyte.iam.domain.exceptions.InvalidCredentialsException;
import com.fragbyte.iam.domain.exceptions.InvalidRefreshTokenException;
import com.fragbyte.iam.domain.exceptions.RoleNotFoundException;
import com.fragbyte.iam.domain.exceptions.UserNotFoundException;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * BC-scoped exception handler for IAM endpoints.
 *
 * <p>Maps the IAM domain exceptions to the standard {@link ApiResponseResource} envelope so
 * authentication failures surface as 401/404/409 instead of a generic 500.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@RestControllerAdvice(basePackages = "com.fragbyte.iam.interfaces.rest")
public class IamExceptionHandler {

  /**
   * Maps a {@link UserNotFoundException} to an HTTP 404 envelope.
   *
   * @param ex the user not found exception
   * @return the envelope with the 404 status
   */
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleUserNotFound(UserNotFoundException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  /**
   * Maps an {@link InvalidCredentialsException} to an HTTP 401 envelope.
   *
   * @param ex the invalid credentials exception
   * @return the envelope with the 401 status
   */
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleInvalidCredentials(
      InvalidCredentialsException ex) {
    return error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  /**
   * Maps an {@link EmailAlreadyExistsException} to an HTTP 409 envelope.
   *
   * @param ex the email already exists exception
   * @return the envelope with the 409 status
   */
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleEmailAlreadyExists(
      EmailAlreadyExistsException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  /**
   * Maps an {@link InvalidRefreshTokenException} to an HTTP 401 envelope.
   *
   * @param ex the invalid refresh token exception
   * @return the envelope with the 401 status
   */
  @ExceptionHandler(InvalidRefreshTokenException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleInvalidRefreshToken(
      InvalidRefreshTokenException ex) {
    return error(HttpStatus.UNAUTHORIZED, ex.getMessage());
  }

  /**
   * Maps an {@link AccountLockedException} to an HTTP 423 envelope.
   *
   * @param ex the account locked exception
   * @return the envelope with the 423 status
   */
  @ExceptionHandler(AccountLockedException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleAccountLocked(AccountLockedException ex) {
    return error(HttpStatus.LOCKED, ex.getMessage());
  }

  /**
   * Maps an {@link AccountNotVerifiedException} to an HTTP 403 envelope.
   *
   * @param ex the account not verified exception
   * @return the envelope with the 403 status
   */
  @ExceptionHandler(AccountNotVerifiedException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleAccountNotVerified(
      AccountNotVerifiedException ex) {
    return error(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  /**
   * Maps an {@link AccountDisabledException} to an HTTP 403 envelope.
   *
   * @param ex the account disabled exception
   * @return the envelope with the 403 status
   */
  @ExceptionHandler(AccountDisabledException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleAccountDisabled(
      AccountDisabledException ex) {
    return error(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  /**
   * Maps an {@link IllegalAccountStateTransitionException} to an HTTP 409 envelope.
   *
   * @param ex the illegal account state transition exception
   * @return the envelope with the 409 status
   */
  @ExceptionHandler(IllegalAccountStateTransitionException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleIllegalAccountStateTransition(
      IllegalAccountStateTransitionException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  /**
   * Maps a {@link RoleNotFoundException} to an HTTP 404 envelope.
   *
   * @param ex the role not found exception
   * @return the envelope with the 404 status
   */
  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleRoleNotFound(RoleNotFoundException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  private ResponseEntity<ApiResponseResource<Void>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(ApiResponseResource.error(status.value(), message));
  }
}
