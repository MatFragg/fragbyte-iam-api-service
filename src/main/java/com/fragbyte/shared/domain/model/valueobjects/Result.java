package com.fragbyte.shared.domain.model.valueobjects;

import org.jspecify.annotations.NullMarked;

import java.util.Optional;
import java.util.function.Function;

/**
 * Result value object.
 *
 * <p>Framework-free container that represents the outcome of an operation, holding either a
 * successful value or an error. Used by domain service ports so the domain layer never depends on
 * framework-specific result or exception types.
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@NullMarked
public sealed interface Result<T, E> {

  /**
   * Represents a successful result containing a value.
   *
   * @param value the success value
   * @param <T> the type of the value
   * @param <E> the type of the error
   */
  record Success<T, E>(T value) implements Result<T, E> {}

  /**
   * Represents a failed result containing error information.
   *
   * @param error the error information
   * @param <T> the type of the value
   * @param <E> the type of the error
   */
  record Failure<T, E>(E error) implements Result<T, E> {}

  /**
   * Creates a successful result with the given value.
   *
   * @param value the success value
   * @param <T> the type of the value
   * @param <E> the type of the error
   * @return a Success result
   */
  static <T, E> Result<T, E> success(T value) {
    return new Success<>(value);
  }

  /**
   * Creates a failed result with the given error.
   *
   * @param error the error information
   * @param <T> the type of the value
   * @param <E> the type of the error
   * @return a Failure result
   */
  static <T, E> Result<T, E> failure(E error) {
    return new Failure<>(error);
  }

  /**
   * Returns true if this result is a success, false if it is a failure.
   *
   * @return true if this result is a success
   */
  default boolean isSuccess() {
    return this instanceof Success;
  }

  /**
   * Returns true if this result is a failure, false if it is a success.
   *
   * @return true if this result is a failure
   */
  default boolean isFailure() {
    return this instanceof Failure;
  }

  /**
   * Returns an {@link Optional} containing the value if this is a success, otherwise an empty
   * {@link Optional}.
   *
   * @return an {@link Optional} with the value or empty
   */
  default Optional<T> toOptional() {
    return switch (this) {
      case Success<T, E> s -> Optional.of(s.value);
      case Failure<T, E> f -> Optional.empty();
    };
  }

  /**
   * Extracts the value if successful, or returns the given default if failed.
   *
   * @param defaultValue the default value
   * @return the value or the default
   */
  default T getOrElse(T defaultValue) {
    return switch (this) {
      case Success<T, E> s -> s.value;
      case Failure<T, E> f -> defaultValue;
    };
  }

  /**
   * Applies a function to the error if this is a failure, otherwise returns this unchanged.
   *
   * @param f the error mapping function
   * @param <E2> the new error type
   * @return a result with the mapped error type
   */
  default <E2> Result<T, E2> mapError(Function<E, E2> f) {
    return switch (this) {
      case Success<T, E> s -> Result.success(s.value);
      case Failure<T, E> failure -> Result.failure(f.apply(failure.error));
    };
  }

  /**
   * Applies a function to the value if this is a success, producing a new Result.
   *
   * @param f the flat mapping function
   * @param <T2> the new value type
   * @return the resulting result
   */
  default <T2> Result<T2, E> flatMap(Function<T, Result<T2, E>> f) {
    return switch (this) {
      case Success<T, E> s -> f.apply(s.value);
      case Failure<T, E> failure -> Result.failure(failure.error);
    };
  }

  /**
   * Applies a function to the value if this is a success.
   *
   * @param f the mapping function
   * @param <T2> the new value type
   * @return a result with the mapped value type
   */
  default <T2> Result<T2, E> map(Function<T, T2> f) {
    return switch (this) {
      case Success<T, E> s -> Result.success(f.apply(s.value));
      case Failure<T, E> failure -> Result.failure(failure.error);
    };
  }

  /**
   * Applies a function to the error if this is a failure. Unlike {@link #mapError}, this takes a
   * Result, allowing fallback recovery.
   *
   * @param f the recovery function
   * @return this result if successful, otherwise the recovered result
   */
  default Result<T, E> recover(Function<E, Result<T, E>> f) {
    return switch (this) {
      case Success<T, E> s -> this;
      case Failure<T, E> failure -> f.apply(failure.error);
    };
  }
}
