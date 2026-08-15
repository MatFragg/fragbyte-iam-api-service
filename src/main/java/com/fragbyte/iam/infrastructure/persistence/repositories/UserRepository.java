package com.hampcoders.glottia.platform.api.iam.infrastructure.persistence.jpa.repositories;

import com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User;
import com.hampcoders.glottia.platform.api.shared.domain.model.valueobjects.UserId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisting and querying {@link User} aggregates. Extends {@link JpaRepository} to
 * provide standard CRUD operations and declares additional queries specific to the IAM bounded
 * context.
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
  Optional<User> findByEmail(String email);

  /**
   * Determines whether a user exists with the specified email address.
   *
   * @param email the user's email address
   * @return {@code true} if a matching user exists; otherwise {@code false}
   */
  boolean existsByEmail(String email);
}
