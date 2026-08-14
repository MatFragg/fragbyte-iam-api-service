package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.Password;

/**
 * Sign in command.
 *
 * <p>This class represents the command to sign in a user.
 *
 * @param email the email of the user.
 * @param password the password of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record SignInCommand(Email email, Password password) {}
