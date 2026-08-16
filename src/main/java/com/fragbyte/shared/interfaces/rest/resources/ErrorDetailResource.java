package com.fragbyte.shared.interfaces.rest.resources;

/**
 * Represents a single field-level error within an {@link ApiResponseResource}.
 *
 * @param field the name of the invalid field
 * @param message the human-readable message describing the validation failure
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ErrorDetailResource(String field, String message) {}
