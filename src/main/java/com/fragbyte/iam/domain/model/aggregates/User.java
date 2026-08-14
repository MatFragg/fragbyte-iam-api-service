package com.fragbyte.iam.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fragbyte.iam.domain.model.events.UserAccessRoleChangedEvent;
import com.fragbyte.iam.domain.model.events.UserEmailChangedEvent;
import com.fragbyte.iam.domain.model.events.UserPasswordChangedEvent;
import com.fragbyte.iam.domain.model.events.UserSignedUpEvent;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.Password;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User aggregate root. <hr> Represents an authenticated user of the platform. Owns the user's
 * authentication credentials and platform-level {@link AccessRoles}.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

  /**
   * Unique identifier of the user. Generated when the Aggregate is created and shared across
   * bounded contexts.
   */
  @EmbeddedId private UserId userId;

  /** Unique email address used for authentication. */
  @Embedded private Email email;

  /** User's hashed password. Plain text passwords should never be stored. */
  @JsonIgnore
  @Embedded
  private Password password;

  /** Platform-level authorization roles. */
  @ElementCollection(fetch = FetchType.EAGER, targetClass = AccessRoles.class)
  @CollectionTable(name = "user_access_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "access_role", nullable = false, length = 20)
  private Set<AccessRoles> accessRoles;

  /** The user constructor as private for jpa operations. */
  protected User() {
    this.accessRoles = new HashSet<>();
  }

  private User(Email email, Password password) {
    this.userId = UserId.newUserId();
    this.email = email;
    this.password = password;
    this.accessRoles = EnumSet.of(AccessRoles.USER);
    validateInvariants();
    publishEvent(new UserSignedUpEvent(this.userId, email));
  }

  /**
   * Creates a new {@code User} aggregate.
   *
   * <p>The user is initialized with the default access role {@link AccessRoles#USER} and a new
   * unique identifier. A {@link UserSignedUpEvent} is published after successful creation.
   *
   * @param email the user's unique email address
   * @param password the hashed password
   * @return a newly created user aggregate
   * @throws IllegalArgumentException if any business invariant is violated
   */
  public static User create(Email email, Password password) {
    return new User(email, password);
  }

  /**
   * Validates the aggregate's business invariants.
   *
   * <p>This method is invoked during aggregate creation to guarantee that the aggregate is always
   * persisted in a valid state.
   *
   * @throws IllegalArgumentException if any invariant is violated
   */
  @Override
  protected void validateInvariants() {
    if (email == null) {
      throw new IllegalArgumentException("Email cannot be null");
    }
    if (password == null) {
      throw new IllegalArgumentException("Password cannot be null");
    }
    if (accessRoles == null || accessRoles.isEmpty()) {
      throw new IllegalArgumentException("User must have at least one access role");
    }
  }

  /**
   * Adds an access role to the user.
   *
   * <p>If the role is not already assigned, the aggregate updates its authorization roles and
   * publishes a {@link UserAccessRoleChangedEvent}.
   *
   * @param role the access role to add
   * @throws NullPointerException if {@code role} is null
   */
  public void addRole(AccessRoles role) {
    Objects.requireNonNull(role, "role cannot be null");
    if (this.accessRoles.add(role)) {
      publishEvent(new UserAccessRoleChangedEvent(this.userId, null, role));
    }
  }

  /**
   * Adds the given access roles to the user.
   *
   * @param roles the access roles to add
   * @throws NullPointerException if {@code roles} is null
   */
  public void addRoles(Collection<AccessRoles> roles) {
    Objects.requireNonNull(roles, "roles cannot be null");
    roles.forEach(this::addRole);
  }

  /**
   * Removes an access role from the user.
   *
   * <p>If the role is currently assigned, the aggregate updates its authorization roles and
   * publishes a {@link UserAccessRoleChangedEvent}.
   *
   * @param role the access role to remove
   * @throws NullPointerException if {@code role} is null
   * @throws IllegalArgumentException if the user would be left without any role
   */
  public void removeRole(AccessRoles role) {
    Objects.requireNonNull(role, "role cannot be null");
    if (this.accessRoles.size() == 1 && this.accessRoles.contains(role)) {
      throw new IllegalArgumentException("User must have at least one access role");
    }
    if (this.accessRoles.remove(role)) {
      publishEvent(new UserAccessRoleChangedEvent(this.userId, role, null));
    }
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
   * @param newHashedPassword the new hashed password
   * @throws NullPointerException if {@code newHashedPassword} is null
   */
  public void changePassword(Password newHashedPassword) {
    Objects.requireNonNull(newHashedPassword, "newHashedPassword cannot be null");
    this.password = newHashedPassword;
    publishEvent(new UserPasswordChangedEvent(this.userId));
  }
}
