package com.fragbyte.iam.interfaces.acl;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;

import java.util.Optional;

/**
 * Anti-corruption layer (ACL) facade exposed by the IAM bounded context.
 *
 * <p>Provides other bounded contexts with a read-only, technology-agnostic view of IAM users
 * without leaking the underlying aggregate or repository.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface IamContextFacade {

  /**
   * Retrieves the user information for the given user identifier.
   *
   * @param userId the user identifier
   * @return an {@link Optional} containing the user information if found; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<UserInfo> getUserByUserId(UserId userId);

  /**
   * Retrieves the user information for the given email address.
   *
   * @param email the email address
   * @return an {@link Optional} containing the user information if found; otherwise, {@link
   *     Optional#empty()}
   */
  Optional<UserInfo> getUserByEmail(Email email);
}
