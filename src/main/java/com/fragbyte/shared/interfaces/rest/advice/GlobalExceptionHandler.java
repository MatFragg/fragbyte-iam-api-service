package com.fragbyte.shared.interfaces.rest.advice;

import com.fragbyte.shared.domain.exceptions.ForbiddenException;
import com.fragbyte.shared.domain.exceptions.ResourceNotFoundException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Locale;

/**
 * Global exception handler that translates exceptions into RFC 7807 Problem Details responses.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String TITLE_BAD_REQUEST = "error.bad-request.title";
  private static final String DETAIL_BAD_REQUEST = "error.bad-request.detail";
  private static final String TITLE_CONFLICT = "error.conflict.title";
  private static final String TITLE_DATA_INTEGRITY = "error.data-integrity.title";
  private static final String DETAIL_DATA_INTEGRITY = "error.data-integrity.detail";
  private static final String TITLE_ACCESS_DENIED = "error.access-denied.title";
  private static final String DETAIL_ACCESS_DENIED = "error.access-denied.detail";
  private static final String TITLE_NOT_FOUND = "error.not-found.title";
  private static final String TITLE_TOO_MANY_REQUESTS = "error.too-many-requests.title";
  private static final String TITLE_INTERNAL = "error.internal.title";
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
   * @return a 400 Bad Request problem detail
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link MethodArgumentNotValidException} for bean validation failures.
   *
   * @param ex the validation exception
   * @return a 400 Bad Request problem detail with the first field error
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    String detail =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                error ->
                    error.getField()
                        + " "
                        + (error.getDefaultMessage() != null
                            ? error.getDefaultMessage()
                            : "is invalid"))
            .findFirst()
            .orElse("Validation failed");
    problem.setDetail(detail);
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link IllegalStateException} for conflict scenarios.
   *
   * @param ex the exception
   * @return a 409 Conflict problem detail
   */
  @ExceptionHandler(IllegalStateException.class)
  public ProblemDetail handleConflict(IllegalStateException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setTitle(message(TITLE_CONFLICT));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link MethodArgumentTypeMismatchException} for bad query or path parameter values.
   *
   * @param ex the exception
   * @return a 400 Bad Request problem detail
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    problem.setDetail(ex.getName() + " must be a valid " + ex.getRequiredType().getSimpleName());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link MissingServletRequestParameterException} for missing required query parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request problem detail
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ProblemDetail handleMissingParameter(MissingServletRequestParameterException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link HttpMessageNotReadableException} for malformed request bodies.
   *
   * @param ex the exception
   * @return a 400 Bad Request problem detail
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    problem.setDetail(message(DETAIL_BAD_REQUEST));
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link ConstraintViolationException} for validated method parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request problem detail with the first violation
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    String detail =
        ex.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
            .findFirst()
            .orElse("Validation failed");
    problem.setDetail(detail);
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link HandlerMethodValidationException} for Spring 6.1 validated parameters.
   *
   * @param ex the exception
   * @return a 400 Bad Request problem detail
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  public ProblemDetail handleMethodValidation(HandlerMethodValidationException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    problem.setTitle(message(TITLE_BAD_REQUEST));
    problem.setDetail(ex.getBody().getDetail());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link RequestNotPermitted} when a rate limit is exceeded.
   *
   * @param ex the exception
   * @return a 429 Too Many Requests problem detail
   */
  @ExceptionHandler(RequestNotPermitted.class)
  public ProblemDetail handleRateLimit(RequestNotPermitted ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.TOO_MANY_REQUESTS);
    problem.setTitle(message(TITLE_TOO_MANY_REQUESTS));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles data integrity violations.
   *
   * @param ex the exception
   * @return a 409 Conflict problem detail
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
    problem.setTitle(message(TITLE_DATA_INTEGRITY));
    problem.setDetail(message(DETAIL_DATA_INTEGRITY));
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles Spring Security access denied exceptions.
   *
   * @param ex the exception
   * @return a 403 Forbidden problem detail
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problem.setTitle(message(TITLE_ACCESS_DENIED));
    problem.setDetail(message(DETAIL_ACCESS_DENIED));
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles domain-level forbidden exceptions.
   *
   * @param ex the exception
   * @return a 403 Forbidden problem detail
   */
  @ExceptionHandler(ForbiddenException.class)
  public ProblemDetail handleForbidden(ForbiddenException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
    problem.setTitle(message(TITLE_ACCESS_DENIED));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles {@link ResponseStatusException} preserving the embedded HTTP status.
   *
   * @param ex the exception
   * @return a problem detail with the embedded status
   */
  @ExceptionHandler(ResponseStatusException.class)
  public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
    var problem = ProblemDetail.forStatus(ex.getStatusCode());
    problem.setDetail(ex.getReason());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles resource not found exceptions.
   *
   * @param ex the exception
   * @return a 404 Not Found problem detail
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problem.setTitle(message(TITLE_NOT_FOUND));
    problem.setDetail(ex.getMessage());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles all unhandled exceptions as internal server errors.
   *
   * @param ex the exception
   * @return a 500 Internal Server Error problem detail
   */
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGeneral(Exception ex) {
    var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problem.setTitle(message(TITLE_INTERNAL));
    problem.setDetail(message(DETAIL_INTERNAL));
    problem.setProperty("timestamp", Instant.now());
    return problem;
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
