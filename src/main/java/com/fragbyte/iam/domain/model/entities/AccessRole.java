package com.fragbyte.iam.domain.model.entities;

import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

/**
 * Access role entity (reference data).
 *
 * <p>Represents a platform-level role persisted in the database and seeded at startup. Roles are
 * effectively immutable reference data: the {@code User} aggregate references them through the
 * {@code user_roles} join table but never mutates them.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Getter
@Entity
public class AccessRole extends AuditableModel {

  /** Database-generated identifier of the role. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** Canonical role name. Seeded names are the stable identity of the role. */
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true, length = 20)
  private AccessRoles name;

  /** Human readable description of what the role grants. */
  @Column(length = 120)
  private String description;

  /** Protected constructor for JPA operations. */
  protected AccessRole() {}

  /**
   * Creates a new access role.
   *
   * @param name the canonical role name
   * @param description a human readable description
   */
  public AccessRole(AccessRoles name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Generates the role as a Spring Security authority.
   *
   * @return the role name prefixed with {@code ROLE_}
   */
  public String asAuthority() {
    return name.asAuthority();
  }
}
