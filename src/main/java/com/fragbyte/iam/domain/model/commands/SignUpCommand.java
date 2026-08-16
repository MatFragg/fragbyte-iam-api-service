package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.RawPassword;

/**
 * Sign up command.
 *
 * <p>This class represents the command to sign up a user. It is a self-service flow: the user is
 * always granted the default platform role and never chooses roles at registration.
 *
 * @param email the email of the user.
 * @param password the password of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record SignUpCommand(Email email, RawPassword password) {

  /**
   * Sign up command constructor.
   *
   * <p>Validates that the raw password meets the minimum strength requirement before it is
   * encrypted.
   *
   * @param email the email of the user.
   * @param password the password of the user.
   * @throws IllegalArgumentException if the password is null or shorter than 8 characters.
   */
  public SignUpCommand {
    if (password == null || password.password() == null || password.password().isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
    if (password.password().length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
  }
}
