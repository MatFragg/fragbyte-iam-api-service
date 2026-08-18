package com.fragbyte.iam.application.internal.commandservices;

import com.fragbyte.iam.application.internal.outboundservices.externalidentity.ExternalIdentityVerifier;
import com.fragbyte.iam.application.internal.outboundservices.externalidentity.VerifiedExternalIdentity;
import com.fragbyte.iam.application.internal.outboundservices.hashing.HashingService;
import com.fragbyte.iam.application.internal.outboundservices.tokens.TokenService;
import com.fragbyte.iam.domain.exceptions.EmailAlreadyExistsException;
import com.fragbyte.iam.domain.exceptions.InvalidCredentialsException;
import com.fragbyte.iam.domain.exceptions.InvalidRefreshTokenException;
import com.fragbyte.iam.domain.exceptions.RoleNotFoundException;
import com.fragbyte.iam.domain.exceptions.UserNotFoundException;
import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.commands.AssignAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.ChangeEmailCommand;
import com.fragbyte.iam.domain.model.commands.ChangePasswordCommand;
import com.fragbyte.iam.domain.model.commands.DisableUserCommand;
import com.fragbyte.iam.domain.model.commands.EnableUserCommand;
import com.fragbyte.iam.domain.model.commands.LinkFederatedIdentityCommand;
import com.fragbyte.iam.domain.model.commands.LockUserCommand;
import com.fragbyte.iam.domain.model.commands.ProvisionUserCommand;
import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;
import com.fragbyte.iam.domain.model.commands.RemoveAccessRoleCommand;
import com.fragbyte.iam.domain.model.commands.SignInCommand;
import com.fragbyte.iam.domain.model.commands.SignInWithProviderCommand;
import com.fragbyte.iam.domain.model.commands.SignUpCommand;
import com.fragbyte.iam.domain.model.commands.UnlinkFederatedIdentityCommand;
import com.fragbyte.iam.domain.model.commands.UnlockUserCommand;
import com.fragbyte.iam.domain.model.commands.VerifyEmailCommand;
import com.fragbyte.iam.domain.model.entities.FederatedIdentity;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import com.fragbyte.iam.domain.model.valueobjects.AuthProvider;
import com.fragbyte.iam.domain.model.valueobjects.PasswordHash;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.domain.services.UserCommandService;
import com.fragbyte.iam.infrastructure.persistence.repositories.AccessRoleRepository;
import com.fragbyte.iam.infrastructure.persistence.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** User command service implementation. {@inheritDoc}
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
@Transactional(noRollbackFor = InvalidCredentialsException.class)
public class UserCommandServiceImpl implements UserCommandService {
  private final UserRepository userRepository;
  private final AccessRoleRepository accessRoleRepository;
  private final HashingService hashingService;
  private final TokenService tokenService;
  private final ExternalIdentityVerifier externalIdentityVerifier;
  private final boolean verificationEnabled;

  /**
   * User command service constructor.
   *
   * @param userRepository the user repository
   * @param accessRoleRepository the access role repository
   * @param hashingService the hashing service
   * @param tokenService the token service
   * @param externalIdentityVerifier the external identity verifier
   * @param verificationEnabled whether email verification is required for new accounts
   */
  public UserCommandServiceImpl(
      UserRepository userRepository,
      AccessRoleRepository accessRoleRepository,
      HashingService hashingService,
      TokenService tokenService,
      ExternalIdentityVerifier externalIdentityVerifier,
      @Value("${iam.verification.enabled:false}") boolean verificationEnabled) {
    this.userRepository = userRepository;
    this.accessRoleRepository = accessRoleRepository;
    this.hashingService = hashingService;
    this.tokenService = tokenService;
    this.externalIdentityVerifier = externalIdentityVerifier;
    this.verificationEnabled = verificationEnabled;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
    var user =
        userRepository
            .findByEmail(command.email())
            .orElseThrow(
                () -> new UserNotFoundException("No user found with email " + command.email().email()));

    user.assertCanSignIn();

    if (!hashingService.matches(
        command.password().password(), user.getPasswordHash().value())) {
      user.recordFailedSignInAttempt();
      userRepository.save(user);
      throw new InvalidCredentialsException();
    }

    user.resetFailedSignInAttempts();
    userRepository.save(user);

    var token =
        tokenService.generateToken(
            user.getEmail().email(), user.getUserId().value(), user.getAccessRoles());

    return Optional.of(ImmutablePair.of(user, token));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignUpCommand command) {
    if (userRepository.existsByEmail(command.email())) {
      throw new EmailAlreadyExistsException(command.email().email());
    }

    var passwordHash = encodePassword(command.password().password());
    var defaultRole =
        accessRoleRepository
            .findByName(AccessRoles.USER)
            .orElseThrow(() -> new RoleNotFoundException(AccessRoles.USER));
    var user =
        User.create(command.email(), passwordHash, Set.of(defaultRole), initialAccountStatus());
    userRepository.save(user);

    var token =
        tokenService.generateToken(
            user.getEmail().email(), user.getUserId().value(), user.getAccessRoles());

    return Optional.of(ImmutablePair.of(user, token));
  }

  /** {@inheritDoc} */
  @Override
  public Optional<ImmutablePair<User, String>> handle(RefreshTokenCommand command) {
    var existingUser =
        userRepository
            .findById(extractUserIdFrom(command.token()))
            .orElseThrow(InvalidRefreshTokenException::new);

    var token =
        tokenService.generateToken(
            existingUser.getEmail().email(),
            existingUser.getUserId().value(),
            existingUser.getAccessRoles());

    return Optional.of(ImmutablePair.of(existingUser, token));
  }

  /** {@inheritDoc} */
  @Override
  public User handle(ProvisionUserCommand command) {
    if (userRepository.existsByEmail(command.email())) {
      throw new EmailAlreadyExistsException(command.email().email());
    }

    var passwordHash = encodePassword(command.password().password());
    var roles =
        command.accessRoles().stream()
            .map(
                name ->
                    accessRoleRepository
                        .findByName(name)
                        .orElseThrow(() -> new RoleNotFoundException(name)))
            .collect(Collectors.toSet());
    var user = User.provision(command.email(), passwordHash, roles, initialAccountStatus());
    return userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(ChangeEmailCommand command) {
    var user = findUser(command.userId());
    if (user.getEmail().equals(command.newEmail())) {
      return;
    }
    if (userRepository.existsByEmail(command.newEmail())) {
      throw new EmailAlreadyExistsException(command.newEmail().email());
    }
    user.changeEmail(command.newEmail());
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(ChangePasswordCommand command) {
    var user = findUser(command.userId());
    if (!hashingService.matches(
        command.currentPassword().password(), user.getPasswordHash().value())) {
      throw new InvalidCredentialsException();
    }
    user.changePassword(encodePassword(command.newPassword().password()));
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(VerifyEmailCommand command) {
    var user = findUser(command.userId());
    user.verifyEmail();
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(LockUserCommand command) {
    var user = findUser(command.userId());
    user.lock();
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(UnlockUserCommand command) {
    var user = findUser(command.userId());
    user.unlock();
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(DisableUserCommand command) {
    var user = findUser(command.userId());
    user.disable();
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(EnableUserCommand command) {
    var user = findUser(command.userId());
    user.enable();
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(AssignAccessRoleCommand command) {
    var user = findUser(command.userId());
    var role =
        accessRoleRepository
            .findByName(command.accessRole())
            .orElseThrow(() -> new RoleNotFoundException(command.accessRole()));
    user.assignAccessRole(role);
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(RemoveAccessRoleCommand command) {
    var user = findUser(command.userId());
    var role =
        accessRoleRepository
            .findByName(command.accessRole())
            .orElseThrow(() -> new RoleNotFoundException(command.accessRole()));
    user.removeAccessRole(role);
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<ImmutablePair<User, String>> handle(SignInWithProviderCommand command) {
    var verifiedIdentity =
        externalIdentityVerifier.verifyToken(command.provider(), command.providerToken());

    // Look up existing user by federated identity
    var existingUser =
        userRepository.findByFederatedIdentityProviderAndFederatedIdentityProviderSubject(
            command.provider(), verifiedIdentity.providerSubject());

    if (existingUser.isPresent()) {
      var user = existingUser.get();
      user.assertCanSignIn();
      user.resetFailedSignInAttempts();
      userRepository.save(user);
      var token =
          tokenService.generateToken(
              user.getEmail().email(), user.getUserId().value(), user.getAccessRoles());
      return Optional.of(ImmutablePair.of(user, token));
    }

    // Check if email matches an existing local user — link the identity
    if (verifiedIdentity.providerEmail() != null) {
      var emailUser = userRepository.findByEmail(new com.fragbyte.iam.domain.model.valueobjects.Email(verifiedIdentity.providerEmail()));
      if (emailUser.isPresent()) {
        var user = emailUser.get();
        var identity =
            new FederatedIdentity(
                command.provider(), verifiedIdentity.providerSubject(), verifiedIdentity.providerEmail());
        user.linkFederatedIdentity(identity);
        userRepository.save(user);
        var token =
            tokenService.generateToken(
                user.getEmail().email(), user.getUserId().value(), user.getAccessRoles());
        return Optional.of(ImmutablePair.of(user, token));
      }
    }

    // Auto-create a new federated-only user
    var defaultRole =
        accessRoleRepository
            .findByName(AccessRoles.USER)
            .orElseThrow(() -> new RoleNotFoundException(AccessRoles.USER));
    var identity =
        new FederatedIdentity(
            command.provider(), verifiedIdentity.providerSubject(), verifiedIdentity.providerEmail());
    var email =
        new com.fragbyte.iam.domain.model.valueobjects.Email(verifiedIdentity.providerEmail());
    var user = User.createWithFederatedIdentity(email, identity, Set.of(defaultRole), initialAccountStatus());
    userRepository.save(user);

    var token =
        tokenService.generateToken(
            user.getEmail().email(), user.getUserId().value(), user.getAccessRoles());
    return Optional.of(ImmutablePair.of(user, token));
  }

  /** {@inheritDoc} */
  @Override
  public void handle(LinkFederatedIdentityCommand command) {
    var user = findUser(command.userId());
    var verifiedIdentity =
        externalIdentityVerifier.verifyToken(command.provider(), command.providerToken());
    var identity =
        new FederatedIdentity(
            command.provider(), verifiedIdentity.providerSubject(), verifiedIdentity.providerEmail());
    user.linkFederatedIdentity(identity);
    userRepository.save(user);
  }

  /** {@inheritDoc} */
  @Override
  public void handle(UnlinkFederatedIdentityCommand command) {
    var user = findUser(command.userId());
    user.unlinkFederatedIdentity(command.provider());
    userRepository.save(user);
  }

  private User findUser(UserId userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new UserNotFoundException("No user found with id " + userId.value()));
  }

  private PasswordHash encodePassword(String rawPassword) {
    return new PasswordHash(hashingService.encode(rawPassword));
  }

  private AccountStatus initialAccountStatus() {
    return verificationEnabled ? AccountStatus.UNVERIFIED : AccountStatus.ACTIVE;
  }

  private UserId extractUserIdFrom(String token) {
    try {
      return new UserId(tokenService.getUserIdFromToken(token));
    } catch (Exception e) {
      throw new InvalidRefreshTokenException();
    }
  }
}
