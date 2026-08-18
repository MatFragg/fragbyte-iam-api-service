package com.fragbyte.iam.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fragbyte.iam.domain.exceptions.AccountDisabledException;
import com.fragbyte.iam.domain.exceptions.AccountLockedException;
import com.fragbyte.iam.domain.exceptions.AccountNotVerifiedException;
import com.fragbyte.iam.domain.exceptions.CannotUnlinkLastAuthMethodException;
import com.fragbyte.iam.domain.exceptions.IllegalAccountStateTransitionException;
import com.fragbyte.iam.domain.model.entities.AccessRole;
import com.fragbyte.iam.domain.model.entities.FederatedIdentity;
import com.fragbyte.iam.domain.model.events.UserAccessRoleAssignedEvent;
import com.fragbyte.iam.domain.model.events.UserAccessRoleRemovedEvent;
import com.fragbyte.iam.domain.model.events.UserDisabledEvent;
import com.fragbyte.iam.domain.model.events.UserEmailChangedEvent;
import com.fragbyte.iam.domain.model.events.UserEmailVerifiedEvent;
import com.fragbyte.iam.domain.model.events.UserEnabledEvent;
import com.fragbyte.iam.domain.model.events.UserFederatedIdentityLinkedEvent;
import com.fragbyte.iam.domain.model.events.UserFederatedIdentityUnlinkedEvent;
import com.fragbyte.iam.domain.model.events.UserLockedEvent;
import com.fragbyte.iam.domain.model.events.UserPasswordChangedEvent;
import com.fragbyte.iam.domain.model.events.UserProvisionedEvent;
import com.fragbyte.iam.domain.model.events.UserSignedUpEvent;
import com.fragbyte.iam.domain.model.events.UserUnlockedEvent;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.PasswordHash;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User aggregate root. <hr> Represents an authenticated user of the platform. Owns the user's
 * authentication credentials, platform-level {@link AccessRoles} and the {@link AccountStatus}
 * lifecycle.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

  /** Maximum number of consecutive failed sign-in attempts before the account is locked. */
  public static final int MAX_FAILED_SIGN_IN_ATTEMPTS = 5;

  /**
   * Unique identifier of the user. Generated when the Aggregate is created and shared across
   * bounded contexts.
   */
  @EmbeddedId private UserId userId;

  /** Unique email address used for authentication. */
  @Embedded private Email email;

  /** User's hashed password. Null for federated-only accounts. Plain text passwords should never be stored. */
  @JsonIgnore
  @Embedded
  private PasswordHash passwordHash;

  /** Platform-level authorization roles. Reference data seeded at startup. */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<AccessRole> roles;

  /** Federated identities linking this user to external authentication providers. */
  @OneToMany(cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private Set<FederatedIdentity> federatedIdentities;

  /** Current account lifecycle state. Governs which operations are allowed. */
  @Enumerated(EnumType.STRING)
  @Column(name = "account_status", nullable = false, length = 20)
  private AccountStatus accountStatus;

  /** Number of consecutive failed sign-in attempts. Reset to zero on a successful sign-in. */
  @Column(name = "failed_sign_in_attempts", nullable = false)
  private int failedSignInAttempts;

  /** The user constructor as private for jpa operations. */
  protected User() {}

  private User(
      Email email, PasswordHash passwordHash, Set<AccessRole> roles, AccountStatus accountStatus) {
    this.userId = UserId.newUserId();
    this.email = email;
    this.passwordHash = passwordHash;
    this.roles = new HashSet<>(roles);
    this.federatedIdentities = new HashSet<>();
    this.accountStatus = accountStatus;
    this.failedSignInAttempts = 0;
    validateInvariants();
  }

  /**
   * Creates a new {@code User} aggregate through the self-service sign-up flow.
   *
   * <p>The user is initialized with the provided roles (the caller supplies the default platform
   * role) and the given initial {@link AccountStatus}. A {@link UserSignedUpEvent} is published
   * after successful creation.
   *
   * @param email the user's unique email address
   * @param passwordHash the hashed password
   * @param roles the initial access roles (must be non-empty)
   * @param accountStatus the initial account status
   * @return a newly created user aggregate
   * @throws IllegalArgumentException if any business invariant is violated
   */
  public static User create(
      Email email,
      PasswordHash passwordHash,
      Set<AccessRole> roles,
      AccountStatus accountStatus) {
    var user = new User(email, passwordHash, roles, accountStatus);
    user.publishEvent(new UserSignedUpEvent(user.userId, email));
    return user;
  }

  /**
   * Provisions a new {@code User} aggregate on behalf of the platform.
   *
   * <p>Used by the administrator provisioning flow. Unlike {@link #create}, a {@link
   * UserProvisionedEvent} is published to distinguish the creation from a self-service sign-up.
   *
   * @param email the user's unique email address
   * @param passwordHash the hashed password
   * @param roles the initial access roles (must be non-empty)
   * @param accountStatus the initial account status
   * @return a newly provisioned user aggregate
   * @throws IllegalArgumentException if any business invariant is violated
   */
  public static User provision(
      Email email,
      PasswordHash passwordHash,
      Set<AccessRole> roles,
      AccountStatus accountStatus) {
    var user = new User(email, passwordHash, roles, accountStatus);
    user.publishEvent(new UserProvisionedEvent(user.userId, email, user.getAccessRoles()));
    return user;
  }

  /**
   * Creates a new {@code User} aggregate through a federated identity provider (e.g. Google).
   *
   * <p>The user has no local password — authentication is handled entirely by the external provider.
   * A {@link UserSignedUpEvent} is published after successful creation.
   *
   * @param email the user's unique email address
   * @param identity the federated identity linking to the external provider
   * @param roles the initial access roles (must be non-empty)
   * @param accountStatus the initial account status
   * @return a newly created user aggregate
   * @throws IllegalArgumentException if any business invariant is violated
   */
  public static User createWithFederatedIdentity(
      Email email,
      FederatedIdentity identity,
      Set<AccessRole> roles,
      AccountStatus accountStatus) {
    var user = new User(email, null, roles, accountStatus);
    user.federatedIdentities.add(identity);
    user.publishEvent(new UserSignedUpEvent(user.userId, email));
    user.publishEvent(
        new UserFederatedIdentityLinkedEvent(
            user.userId, identity.getProvider(), identity.getProviderSubject()));
    return user;
  }

  /**
   * Validates the aggregate's business invariants.
   *
   * <p>This method is invoked during aggregate creation and before persistence operations to
   * guarantee that the aggregate is always persisted in a valid state.
   *
   * @throws IllegalArgumentException if any invariant is violated
   */
  @Override
  protected void validateInvariants() {
    if (email == null) {
      throw new IllegalArgumentException("Email cannot be null");
    }
    if (!hasAtLeastOneAuthMethod()) {
      throw new IllegalArgumentException(
          "User must have at least one authentication method (password or federated identity)");
    }
    if (accountStatus == null) {
      throw new IllegalArgumentException("AccountStatus cannot be null");
    }
    if (roles == null || roles.isEmpty()) {
      throw new IllegalArgumentException("User must have at least one access role");
    }
  }

  private boolean hasAtLeastOneAuthMethod() {
    return passwordHash != null || (federatedIdentities != null && !federatedIdentities.isEmpty());
  }

  /**
   * Returns the user's access roles as domain value objects.
   *
   * @return an unmodifiable set of the user's roles
   */
  public Set<AccessRoles> getAccessRoles() {
    return roles.stream().map(AccessRole::getName).collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Determines whether the user holds the given access role.
   *
   * @param roleName the role to check
   * @return {@code true} if the user holds the role; otherwise {@code false}
   */
  public boolean hasAccessRole(AccessRoles roleName) {
    return roles.stream().anyMatch(role -> role.getName() == roleName);
  }

  /**
   * Asserts that the account is allowed to sign in.
   *
   * @throws AccountDisabledException if the account is disabled
   * @throws AccountLockedException if the account is locked
   * @throws AccountNotVerifiedException if the account has not been verified
   */
  public void assertCanSignIn() {
    if (accountStatus == AccountStatus.DISABLED) {
      throw new AccountDisabledException();
    }
    if (accountStatus == AccountStatus.LOCKED) {
      throw new AccountLockedException();
    }
    if (accountStatus == AccountStatus.UNVERIFIED) {
      throw new AccountNotVerifiedException();
    }
  }

  /**
   * Records a failed sign-in attempt.
   *
   * <p>When the number of consecutive failures reaches {@link #MAX_FAILED_SIGN_IN_ATTEMPTS}, the
   * account is automatically locked and a {@link UserLockedEvent} is published.
   */
  public void recordFailedSignInAttempt() {
    if (accountStatus != AccountStatus.ACTIVE) {
      return;
    }
    failedSignInAttempts++;
    if (failedSignInAttempts >= MAX_FAILED_SIGN_IN_ATTEMPTS) {
      failedSignInAttempts = 0;
      accountStatus = AccountStatus.LOCKED;
      publishEvent(new UserLockedEvent(userId));
    }
  }

  /**
   * Resets the consecutive failed sign-in attempt counter after a successful sign-in.
   */
  public void resetFailedSignInAttempts() {
    failedSignInAttempts = 0;
  }

  /**
   * Verifies the user's email address, transitioning the account from {@code UNVERIFIED} to {@code
   * ACTIVE}.
   *
   * @throws IllegalAccountStateTransitionException if the account is not {@code UNVERIFIED}
   */
  public void verifyEmail() {
    assertState(AccountStatus.UNVERIFIED, "Only an unverified account can be verified");
    accountStatus = AccountStatus.ACTIVE;
    publishEvent(new UserEmailVerifiedEvent(userId));
  }

  /**
   * Locks the account, temporarily suspending access to the platform.
   *
   * <p>No-op when the account is already locked.
   *
   * @throws IllegalAccountStateTransitionException if the account is disabled
   */
  public void lock() {
    if (accountStatus == AccountStatus.LOCKED) {
      return;
    }
    assertStateNot(AccountStatus.DISABLED, "A disabled account cannot be locked");
    accountStatus = AccountStatus.LOCKED;
    publishEvent(new UserLockedEvent(userId));
  }

  /**
   * Unlocks the account, transitioning it back to {@code ACTIVE}.
   *
   * <p>No-op when the account is already active.
   *
   * @throws IllegalAccountStateTransitionException if the account is unverified or disabled
   */
  public void unlock() {
    if (accountStatus == AccountStatus.ACTIVE) {
      return;
    }
    assertStateNot(AccountStatus.UNVERIFIED, "An unverified account cannot be unlocked");
    assertStateNot(AccountStatus.DISABLED, "A disabled account cannot be unlocked");
    accountStatus = AccountStatus.ACTIVE;
    failedSignInAttempts = 0;
    publishEvent(new UserUnlockedEvent(userId));
  }

  /**
   * Re-enables the account, transitioning it from {@code DISABLED} back to {@code ACTIVE}.
   *
   * <p>No-op when the account is already enabled.
   *
   * @throws IllegalAccountStateTransitionException if the account is not {@code DISABLED}
   */
  public void enable() {
    if (accountStatus == AccountStatus.ACTIVE) {
      return;
    }
    assertState(AccountStatus.DISABLED, "Only a disabled account can be enabled");
    accountStatus = AccountStatus.ACTIVE;
    failedSignInAttempts = 0;
    publishEvent(new UserEnabledEvent(userId));
  }

  /**
   * Disables the account, preventing any future sign-in.
   *
   * <p>No-op when the account is already disabled. Can be reversed via {@link #enable()}.
   */
  public void disable() {
    if (accountStatus == AccountStatus.DISABLED) {
      return;
    }
    accountStatus = AccountStatus.DISABLED;
    publishEvent(new UserDisabledEvent(userId));
  }

  /**
   * Changes the user's email address.
   *
   * <p>If the new email is different from the current one, the aggregate updates its state and
   * publishes a {@link UserEmailChangedEvent}.
   *
   * @param newEmail the new email address
   * @throws NullPointerException if {@code newEmail} is null
   */
  public void changeEmail(Email newEmail) {
    Objects.requireNonNull(newEmail, "newEmail cannot be null");
    if (this.email.equals(newEmail)) {
      return;
    }
    this.email = newEmail;
    publishEvent(new UserEmailChangedEvent(this.userId, newEmail));
  }

  /**
   * Updates the user's hashed password.
   *
   * <p>A {@link UserPasswordChangedEvent} is published after the password has been successfully
   * updated.
   *
   * @param newPasswordHash the new hashed password
   * @throws NullPointerException if {@code newPasswordHash} is null
   */
  public void changePassword(PasswordHash newPasswordHash) {
    Objects.requireNonNull(newPasswordHash, "newPasswordHash cannot be null");
    this.passwordHash = newPasswordHash;
    publishEvent(new UserPasswordChangedEvent(this.userId));
  }

  /**
   * Links a federated identity to the user account.
   *
   * <p>If the user already has an identity from the same provider, the operation is a no-op.
   * Otherwise a {@link UserFederatedIdentityLinkedEvent} is published.
   *
   * @param identity the federated identity to link
   * @throws CannotUnlinkLastAuthMethodException if this would leave the user with no auth method
   * @throws NullPointerException if {@code identity} is null
   */
  public void linkFederatedIdentity(FederatedIdentity identity) {
    Objects.requireNonNull(identity, "identity cannot be null");
    if (hasFederatedIdentity(identity.getProvider())) {
      return;
    }
    federatedIdentities.add(identity);
    publishEvent(
        new UserFederatedIdentityLinkedEvent(
            userId, identity.getProvider(), identity.getProviderSubject()));
  }

  /**
   * Unlinks a federated identity from the user account.
   *
   * <p>A user must always retain at least one authentication method. If the user does not have an
   * identity from the given provider, the operation is a no-op.
   *
   * @param provider the provider to unlink
   * @throws CannotUnlinkLastAuthMethodException if this would leave the user with no auth method
   */
  public void unlinkFederatedIdentity(AuthProvider provider) {
    Objects.requireNonNull(provider, "provider cannot be null");
    if (!hasFederatedIdentity(provider)) {
      return;
    }
    if (passwordHash == null && federatedIdentities.size() <= 1) {
      throw new CannotUnlinkLastAuthMethodException();
    }
    federatedIdentities.removeIf(identity -> identity.getProvider() == provider);
    publishEvent(new UserFederatedIdentityUnlinkedEvent(userId, provider));
  }

  /**
   * Determines whether the user has a federated identity from the given provider.
   *
   * @param provider the provider to check
   * @return {@code true} if the user has a federated identity from the provider
   */
  public boolean hasFederatedIdentity(AuthProvider provider) {
    return federatedIdentities.stream()
        .anyMatch(identity -> identity.getProvider() == provider);
  }

  /**
   * Grants a new platform access role to the user.
   *
   * <p>If the user already holds the role, the operation is a no-op. Otherwise a {@link
   * UserAccessRoleAssignedEvent} is published.
   *
   * @param role the access role to grant
   * @throws NullPointerException if {@code role} is null
   */
  public void assignAccessRole(AccessRole role) {
    Objects.requireNonNull(role, "role cannot be null");
    if (hasAccessRole(role.getName())) {
      return;
    }
    roles.add(role);
    publishEvent(new UserAccessRoleAssignedEvent(userId, role.getName()));
  }

  /**
   * Revokes an access role from the user.
   *
   * <p>A user must always retain at least one access role. If the user does not hold the role, the
   * operation is a no-op.
   *
   * @param role the access role to revoke
   * @throws IllegalAccountStateTransitionException if this would leave the user without roles
   * @throws NullPointerException if {@code role} is null
   */
  public void removeAccessRole(AccessRole role) {
    Objects.requireNonNull(role, "role cannot be null");
    if (!hasAccessRole(role.getName())) {
      return;
    }
    if (roles.size() <= 1) {
      throw new IllegalAccountStateTransitionException(
          "A user must always retain at least one access role");
    }
    roles.removeIf(r -> r.getName() == role.getName());
    publishEvent(new UserAccessRoleRemovedEvent(userId, role.getName()));
  }

  private void assertState(AccountStatus expected, String message) {
    if (accountStatus != expected) {
      throw new IllegalAccountStateTransitionException(message);
    }
  }

  private void assertStateNot(AccountStatus forbidden, String message) {
    if (accountStatus == forbidden) {
      throw new IllegalAccountStateTransitionException(message);
    }
  }
}
