package com.fragbyte.shared.interfaces.rest.advice;

import com.fragbyte.shared.domain.exceptions.ForbiddenException;
import com.fragbyte.shared.domain.exceptions.ResourceNotFoundException;
import com.fragbyte.shared.interfaces.rest.resources.ApiResponseResource;
import com.fragbyte.shared.interfaces.rest.resources.ErrorDetailResource;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/**
 * Global exception handler that translates exceptions into the standard {@link
 * ApiResponseResource} envelope.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String MESSAGE_VALIDATION = "Validation failed";
  private static final String DETAIL_BAD_REQUEST = "error.bad-request.detail";
  private static final String DETAIL_DATA_INTEGRITY = "error.data-integrity.detail";
  private static final String DETAIL_ACCESS_DENIED = "error.access-denied.detail";
  private static final String TITLE_TOO_MANY_REQUESTS = "error.too-many-requests.title";
  private static final String DETAIL_INTERNAL = "error.internal.detail";

  private final MessageSource messageSource;

  /**
   * Constructs the handler with the message source for i18n.
   *
   * @param messageSource the message source
   */
  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Handles {@link IllegalArgumentException} for bad request scenarios.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleBadRequest(IllegalArgumentException ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  /**
   * Handles {@link MethodArgumentNotValidException} for bean validation failures.
   *
   * @param ex the validation exception
   * @return a 400 Bad Request envelope with the field-level errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleValidation(
      MethodArgumentNotValidException ex) {
    List<ErrorDetailResource> errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    new ErrorDetailResource(
                        error.getField(),
                        error.getDefaultMessage() != null
                            ? error.getDefaultMessage()
                            : "is invalid"))
            .toList();
    return validation(HttpStatus.BAD_REQUEST, MESSAGE_VALIDATION, errors);
  }

  /**
   * Handles {@link IllegalStateException} for conflict scenarios.
   *
   * @param ex the exception
   * @return a 409 Conflict envelope
   */
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleConflict(IllegalStateException ex) {
    return error(HttpStatus.CONFLICT, ex.getMessage());
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException} for bad query or path parameter values.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex) {
    return error(
        HttpStatus.BAD_REQUEST,
        ex.getName() + " must be a valid " + ex.getRequiredType().getSimpleName());
  }

  /**
   * Handles {@link MissingServletRequestParameterException} for missing required query parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleMissingParameter(
      MissingServletRequestParameterException ex) {
    return error(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  /**
   * Handles {@link HttpMessageNotReadableException} for malformed request bodies.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleNotReadable(
      HttpMessageNotReadableException ex) {
    return error(HttpStatus.BAD_REQUEST, message(DETAIL_BAD_REQUEST));
  }

  /**
   * Handles {@link ConstraintViolationException} for validated method parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope with the field-level errors
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleConstraintViolation(
      ConstraintViolationException ex) {
    List<ErrorDetailResource> errors =
        ex.getConstraintViolations().stream()
            .map(
                violation ->
                    new ErrorDetailResource(
                        violation.getPropertyPath().toString(), violation.getMessage()))
            .toList();
    return validation(HttpStatus.BAD_REQUEST, MESSAGE_VALIDATION, errors);
  }

  /**
   * Handles {@link HandlerMethodValidationException} for Spring 6.1 validated parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request envelope
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleMethodValidation(
      HandlerMethodValidationException ex) {
    return error(HttpStatus.BAD_REQUEST, MESSAGE_VALIDATION);
  }

  /**
   * Handles {@link RequestNotPermitted} when a rate limit is exceeded.
   *
   * @param ex the exception
   * @return a 429 Too Many Requests envelope
   */
  @ExceptionHandler(RequestNotPermitted.class)
  public ResponseEntity<ApiResponseResource<Void>> handleRateLimit(RequestNotPermitted ex) {
    return error(HttpStatus.TOO_MANY_REQUESTS, message(TITLE_TOO_MANY_REQUESTS));
  }

  /**
   * Handles data integrity violations.
   *
   * @param ex the exception
   * @return a 409 Conflict envelope
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleDataIntegrity(
      DataIntegrityViolationException ex) {
    return error(HttpStatus.CONFLICT, message(DETAIL_DATA_INTEGRITY));
  }

  /**
   * Handles Spring Security access denied exceptions.
   *
   * @param ex the exception
   * @return a 403 Forbidden envelope
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleAccessDenied(AccessDeniedException ex) {
    return error(HttpStatus.FORBIDDEN, message(DETAIL_ACCESS_DENIED));
  }

  /**
   * Handles domain-level forbidden exceptions.
   *
   * @param ex the exception
   * @return a 403 Forbidden envelope
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleForbidden(ForbiddenException ex) {
    return error(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  /**
   * Handles {@link ResponseStatusException} preserving the embedded HTTP status.
   *
   * @param ex the exception
   * @return an envelope with the embedded status
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleResponseStatus(
      ResponseStatusException ex) {
    return error(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason());
  }

  /**
   * Handles resource not found exceptions.
   *
   * @param ex the exception
   * @return a 404 Not Found envelope
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponseResource<Void>> handleNotFound(ResourceNotFoundException ex) {
    return error(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  /**
   * Handles all unhandled exceptions as internal server errors.
   *
   * @param ex the exception
   * @return a 500 Internal Server Error envelope
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponseResource<Void>> handleGeneral(Exception ex) {
    return error(HttpStatus.INTERNAL_SERVER_ERROR, message(DETAIL_INTERNAL));
  }

  private ResponseEntity<ApiResponseResource<Void>> error(HttpStatus status, String message) {
    return ResponseEntity.status(status)
        .body(ApiResponseResource.error(status.value(), message));
  }

  private ResponseEntity<ApiResponseResource<Void>> validation(
      HttpStatus status, String message, List<ErrorDetailResource> errors) {
    return ResponseEntity.status(status)
        .body(ApiResponseResource.validation(status.value(), message, errors));
  }

  private String message(String key) {
    try {
      Locale locale = LocaleContextHolder.getLocale();
      return messageSource.getMessage(key, null, locale);
    } catch (NoSuchMessageException e) {
      return key;
    }
  }
}
