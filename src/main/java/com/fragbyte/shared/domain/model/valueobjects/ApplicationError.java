package com.fragbyte.shared.domain.model.valueobjects;

import org.jspecify.annotations.NullMarked;

/**
 * Application error value object.
 *
 * <p>Represents an error that occurred while executing an application use case. Carries a
 * machine-readable code, a human-readable message and optional extra details.
 *
 * @param code the machine-readable error code
 * @param message the human-readable error message
 * @param details optional extra details about the error
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@NullMarked
public record ApplicationError(String code, String message, String details) {

  /**
   * Creates an ApplicationError with code and message only.
   *
   * @param code the machine-readable error code
   * @param message the human-readable error message
   */
  public ApplicationError(String code, String message) {
    this(code, message, null);
  }

  /**
   * Creates a validation error: input data is invalid or violates constraints.
   *
   * @param fieldOrConcept the field or concept that failed validation
   * @param reason the reason the validation failed
   * @return a validation error
   */
  public static ApplicationError validationError(String fieldOrConcept, String reason) {
    return new ApplicationError(
        "VALIDATION_ERROR", "Validation failed: %s".formatted(fieldOrConcept), reason);
  }

  /**
   * Creates a not found error: the requested resource does not exist.
   *
   * @param resourceType the type of the missing resource
   * @param identifier the identifier of the missing resource
   * @return a not found error
   */
  public static ApplicationError notFound(String resourceType, String identifier) {
    return new ApplicationError(
        "%s_NOT_FOUND".formatted(resourceType.toUpperCase()),
        "%s not found: %s".formatted(resourceType, identifier),
        null);
  }

  /**
   * Creates a business rule violation error: the operation violates domain constraints.
   *
   * @param rule the violated business rule
   * @param reason the reason the rule was violated
   * @return a business rule violation error
   */
  public static ApplicationError businessRuleViolation(String rule, String reason) {
    return new ApplicationError(
        "BUSINESS_RULE_VIOLATION", "Business rule violation: %s".formatted(rule), reason);
  }

  /**
   * Creates a conflict error: the operation cannot be completed due to conflicting state.
   *
   * @param resource the conflicting resource
   * @param reason the reason the operation conflicts
   * @return a conflict error
   */
  public static ApplicationError conflict(String resource, String reason) {
    return new ApplicationError(
        "%s_CONFLICT".formatted(resource.toUpperCase()), "Conflict with %s".formatted(resource),
        reason);
  }

  /**
   * Creates an unexpected error: something went wrong that shouldn't have.
   *
   * @param context the context where the error occurred
   * @param reason the reason of the unexpected error
   * @return an unexpected error
   */
  public static ApplicationError unexpected(String context, String reason) {
    return new ApplicationError(
        "UNEXPECTED_ERROR", "Unexpected error in %s".formatted(context), reason);
  }
}
