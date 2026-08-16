package com.fragbyte.iam.domain.model.commands;

import com.fragbyte.iam.domain.model.valueobjects.RawPassword;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

/**
 * Change password command.
 *
 * <p>This class represents the command to change the password of a user. The current password must
 * be provided and verified before the new one is applied.
 *
 * @param userId the identifier of the user.
 * @param currentPassword the current password of the user.
 * @param newPassword the new password of the user.
 * @see com.fragbyte.iam.domain.model.aggregates.User
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public record ChangePasswordCommand(
    UserId userId, RawPassword currentPassword, RawPassword newPassword) {

  /**
   * The change password command constructor.
   *
   * @throws IllegalArgumentException if the new password is shorter than 8 characters.
   */
  public ChangePasswordCommand {
    if (newPassword == null || newPassword.password() == null || newPassword.password().isBlank()) {
      throw new IllegalArgumentException("New password cannot be null or blank");
    }
    if (newPassword.password().length() < 8) {
      throw new IllegalArgumentException("New password must be at least 8 characters");
    }
  }
}
