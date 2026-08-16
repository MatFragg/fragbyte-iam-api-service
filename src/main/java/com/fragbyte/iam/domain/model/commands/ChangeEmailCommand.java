package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Change email command.
 *
 * <p>This class represents the command to change the email address of a user.
 *
 * @param userId the identifier of the user.
 * @param newEmail the new email address of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ChangeEmailCommand(UserId userId, Email newEmail) {}
