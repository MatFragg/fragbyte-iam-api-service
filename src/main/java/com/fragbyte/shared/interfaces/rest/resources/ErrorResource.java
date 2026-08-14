package com.fragbyte.shared.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;

/**
 * Standard error response resource returned from REST endpoints.
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResource(
  String code,
  String message,
  @Nullable String details) {

  /**
   * Creates an ErrorResource from code and message.
   */
  public ErrorResource(String code, String message) {
    this(code, message, null);
  }
}