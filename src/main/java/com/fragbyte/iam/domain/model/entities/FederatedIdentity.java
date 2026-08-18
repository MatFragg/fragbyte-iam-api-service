package com.fragbyte.iam.domain.model.entities;

import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.shared.domain.model.entities.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

/**
 * Federated identity entity.
 *
 * <p>Represents a link between a {@link com.fragbyte.iam.domain.model.aggregates.User} and an
 * external identity provider (e.g. Google). A user may have multiple federated identities, but at
 * most one per provider.
 *
 * <p>This entity is part of the User aggregate boundary and is never accessed independently.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Getter
@Entity
@Table(
    name = "user_federated_identities",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_federated_identity_provider_subject",
            columnNames = {"provider", "provider_subject"}))
public class FederatedIdentity extends AuditableModel {

  /** Database-generated identifier. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** The external authentication provider. */
  @Enumerated(EnumType.STRING)
  @Column(name = "provider", nullable = false, length = 20)
  private AuthProvider provider;

  /** The unique subject identifier assigned by the provider (the "sub" claim). */
  @Column(name = "provider_subject", nullable = false)
  private String providerSubject;

  /** The email address reported by the provider (may differ from the local email). */
  @Column(name = "provider_email")
  private String providerEmail;

  /** Protected constructor for JPA operations. */
  protected FederatedIdentity() {}

  /**
   * Creates a new federated identity.
   *
   * @param provider the external authentication provider
   * @param providerSubject the unique subject identifier from the provider
   * @param providerEmail the email address from the provider (may be null)
   */
  public FederatedIdentity(AuthProvider provider, String providerSubject, String providerEmail) {
    if (provider == null) {
      throw new IllegalArgumentException("Provider cannot be null");
    }
    if (providerSubject == null || providerSubject.isBlank()) {
      throw new IllegalArgumentException("Provider subject cannot be null or blank");
    }
    this.provider = provider;
    this.providerSubject = providerSubject;
    this.providerEmail = providerEmail;
  }

  /**
   * Creates a new federated identity without a provider email.
   *
   * @param provider the external authentication provider
   * @param providerSubject the unique subject identifier from the provider
   */
  public FederatedIdentity(AuthProvider provider, String providerSubject) {
    this(provider, providerSubject, null);
  }
}
