package com.fragbyte.iam.infrastructure.persistence.repositories;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for persisting and querying {@link User} aggregates. Extends {@link JpaRepository} to
 * provide standard CRUD operations and declares additional queries specific to the IAM bounded
 * context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
  /**
   * Retrieves a user by email address.
   *
   * @param email the user's email address
   * @return an {@link Optional} containing the matching user if found; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<User> findByEmail(Email email);

  /**
   * Determines whether a user exists with the specified email address.
   *
   * @param email the user's email address
   * @return {@code true} if a matching user exists; otherwise {@code false}
   */
  boolean existsByEmail(Email email);

  /**
   * Retrieves a user by their federated identity.
   *
   * @param provider the external authentication provider
   * @param providerSubject the unique subject identifier from the provider
   * @return an {@link Optional} containing the matching user if found; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<User> findByFederatedIdentitiesProviderAndFederatedIdentitiesProviderSubject(
      AuthProvider provider, String providerSubject);
}
