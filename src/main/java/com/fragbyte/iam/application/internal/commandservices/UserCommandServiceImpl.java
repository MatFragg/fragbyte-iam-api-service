package com.fragbyte.iam.application.internal.commandservices;

import com.fragbyte.iam.application.internal.outboundservices.hashing.HashingService;
import com.fragbyte.iam.application.internal.outboundservices.tokens.TokenService;
import com.fragbyte.iam.domain.exceptions.EmailAlreadyExistsException;
import com.fragbyte.iam.domain.exceptions.InvalidCredentialsException;
import com.fragbyte.iam.domain.exceptions.InvalidRefreshTokenException;
import com.fragbyte.iam.domain.exceptions.UserNotFoundException;
import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;
import com.fragbyte.iam.domain.model.commands.SignInCommand;
import com.fragbyte.iam.domain.model.commands.SignUpCommand;
import com.fragbyte.iam.domain.model.valueobjects.Password;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.domain.services.UserCommandService;
import com.fragbyte.iam.infrastructure.persistence.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/** User command service implementation. {@inheritDoc}
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
@Transactional
public class UserCommandServiceImpl implements UserCommandService {
  private final UserRepository userRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;

  /**
   * User command service constructor.
   *
   * @param userRepository the user repository
   * @param hashingService the hashing service
   * @param tokenService the token service
   */
  public UserCommandServiceImpl(
    UserRepository userRepository,
    HashingService hashingService,
    TokenService tokenService) {
    this.userRepository = userRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
    var user = userRepository.findByEmail(command.email());

    if (user.isEmpty()) {
      throw new UserNotFoundException("No user found with email " + command.email().email());
    }

    if (!hashingService.matches(command.password().password(), user.get().getPassword().password())) {
      throw new InvalidCredentialsException();
    }

    var authenticatedUser = user.get();
    var userId = authenticatedUser.getUserId().value();

    var token =
      tokenService.generateToken(
        authenticatedUser.getEmail().email(),
        userId,
        authenticatedUser.getAccessRoles());

    return Optional.of(ImmutablePair.of(authenticatedUser, token));
  }

  /** {@inheritDoc} */
  @Override
  @Transactional
  public Optional<ImmutablePair<User, String>> handle(SignUpCommand command) {
    if (userRepository.existsByEmail(command.email())) {
      throw new EmailAlreadyExistsException(command.email().email());
    }

    var hashedPassword = hashingService.encode(command.password().password());
    var user = User.create(command.email(), new Password(hashedPassword));
    userRepository.save(user);

    var userId = user.getUserId().value();
    var token =
      tokenService.generateToken(
        user.getEmail().email(),
        userId,
        user.getAccessRoles());

    return Optional.of(ImmutablePair.of(user, token));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<ImmutablePair<User, String>> handle(RefreshTokenCommand command) {
    var existingUser =
        userRepository
            .findById(extractUserIdFrom(command.token()))
            .orElseThrow(InvalidRefreshTokenException::new);
    var userId = existingUser.getUserId().value();

    var token =
      tokenService.generateToken(
        existingUser.getEmail().email(),
        userId,
        existingUser.getAccessRoles());

    return Optional.of(ImmutablePair.of(existingUser, token));
  }

  private UserId extractUserIdFrom(String token) {
    try {
      return new UserId(tokenService.getUserIdFromToken(token));
    } catch (Exception e) {
      throw new InvalidRefreshTokenException();
    }
  }
}
