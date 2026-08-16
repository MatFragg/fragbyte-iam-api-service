package com.fragbyte.iam.application.internal.queryservices;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.queries.GetAllUsersQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByEmailQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.iam.domain.services.UserQueryService;
import com.fragbyte.iam.infrastructure.persistence.repositories.UserRepository;
import com.fragbyte.shared.domain.model.valueobjects.Paged;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** Implementation of {@link UserQueryService} interface.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
  private final UserRepository userRepository;

  /**
   * Constructor.
   *
   * @param userRepository {@link UserRepository} instance.
   */
  public UserQueryServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * This method is used to handle {@link GetAllUsersQuery} query.
   *
   * @param query {@link GetAllUsersQuery} instance
   * @return {@link Paged} of {@link User} instances
   * @see GetAllUsersQuery
   */
  @Override
  public Paged<User> handle(GetAllUsersQuery query) {
    var pageable = PageRequest.of(query.page(), query.size());
    var page = userRepository.findAll(pageable);
    return Paged.of(page.getContent(), page.getTotalElements(), query.page(), query.size());
  }

  /**
   * This method is used to handle {@link GetUserByIdQuery} query.
   *
   * @param query {@link GetUserByIdQuery} instance
   * @return {@link Optional} of {@link User} instance
   * @see GetUserByIdQuery
   */
  @Override
  public Optional<User> handle(GetUserByIdQuery query) {
    return userRepository.findById(query.userId());
  }

  /**
   * This method is used to handle {@link GetUserByEmailQuery} query.
   *
   * @param query {@link GetUserByEmailQuery} instance
   * @return {@link Optional} of {@link User} instance
   * @see GetUserByEmailQuery
   */
  @Override
  public Optional<User> handle(GetUserByEmailQuery query) {
    return userRepository.findByEmail(query.email());
  }
}
