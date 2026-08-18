package com.fragbyte.iam.domain.model.valueobjects;

/**
 * Account status for the {@code User} aggregate lifecycle.
 *
 * <p>Governs which authentication and account-management operations are permitted. Transitions
 * between states are guarded by the aggregate (State pattern).
 *
 * <ul>
 *   <li>{@code UNVERIFIED}: registered but the email address has not been verified yet.
 *   <li>{@code ACTIVE}: fully operational; the user can sign in.
 *   <li>{@code LOCKED}: temporarily suspended (e.g. too many failed sign-in attempts).
 *   <li>{@code DISABLED}: deactivated; can be re-enabled by an administrator.
 * </ul>
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public enum AccountStatus {
  UNVERIFIED,
  ACTIVE,
  LOCKED,
  DISABLED;
}
