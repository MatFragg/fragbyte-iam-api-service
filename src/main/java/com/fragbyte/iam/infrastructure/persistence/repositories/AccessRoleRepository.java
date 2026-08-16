package com.fragbyte.iam.infrastructure.persistence.repositories;

import com.fragbyte.iam.domain.model.entities.AccessRole;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for the {@link AccessRole} reference data.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Repository
public interface AccessRoleRepository extends JpaRepository<AccessRole, Long> {

  /**
   * Retrieves an access role by its canonical name.
   *
   * @param name the role name
   * @return an {@link Optional} containing the matching role if found; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<AccessRole> findByName(AccessRoles name);

  /**
   * Determines whether an access role with the given name exists.
   *
   * @param name the role name
   * @return {@code true} if a matching role exists; otherwise {@code false}
   */
  boolean existsByName(AccessRoles name);
}
