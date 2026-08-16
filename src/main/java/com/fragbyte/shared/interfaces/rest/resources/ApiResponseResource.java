package com.fragbyte.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import java.time.Instant;
import java.util.List;

/**
 * Standard envelope returned by every REST endpoint, for both successful and failed responses.
 *
 * <p>Success responses carry the payload in {@code data}; error responses carry a human-readable
 * {@code message} and, for validation failures, a list of {@link ErrorDetailResource}s.
 *
 * @param <T> the type of the payload carried in {@code data}
 * @param status the HTTP status of the response
 * @param success whether the operation succeeded
 * @param message a human-readable message describing the outcome
 * @param data the response payload, {@code null} on errors
 * @param errors field-level errors, present only on validation failures
 * @param timestamp when the response was generated
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponseResource<T>(
    int status,
    boolean success,
    String message,
    @Nullable T data,
    @Nullable List<ErrorDetailResource> errors,
    Instant timestamp) {

  /**
   * Builds a successful response envelope.
   *
   * @param status the HTTP status
   * @param message a human-readable success message
   * @param data the response payload
   * @param <T> the payload type
   * @return the envelope
   */
  public static <T> ApiResponseResource<T> success(int status, String message, T data) {
    return new ApiResponseResource<>(status, true, message, data, null, Instant.now());
  }

  /**
   * Builds an error response envelope without field-level details.
   *
   * @param status the HTTP status
   * @param message a human-readable error message
   * @param <T> the payload type
   * @return the envelope
   */
  public static <T> ApiResponseResource<T> error(int status, String message) {
    return new ApiResponseResource<>(status, false, message, null, null, Instant.now());
  }

  /**
   * Builds an error response envelope carrying field-level validation details.
   *
   * @param status the HTTP status
   * @param message a human-readable error message
   * @param errors the field-level errors
   * @param <T> the payload type
   * @return the envelope
   */
  public static <T> ApiResponseResource<T> validation(
      int status, String message, List<ErrorDetailResource> errors) {
    return new ApiResponseResource<>(status, false, message, null, errors, Instant.now());
  }
}
