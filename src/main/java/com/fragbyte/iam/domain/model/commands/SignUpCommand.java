package com.hampcoders.glottia.platform.api.iam.domain.model.commands;

/**
 * Sign up command.
 *
 * <p>This class represents the command to sign up a user.
 *
 * @param email the email of the user.
 * @param password the password of the user.
 * @see com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User
 */
public record SignUpCommand(String email, String password) {}
