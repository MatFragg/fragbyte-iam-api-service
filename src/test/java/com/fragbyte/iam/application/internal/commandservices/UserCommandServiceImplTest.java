package com.fragbyte.iam.application.internal.commandservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fragbyte.iam.application.internal.outboundservices.externalidentity.ExternalIdentityVerifier;
import com.fragbyte.iam.application.internal.outboundservices.hashing.HashingService;
import com.fragbyte.iam.application.internal.outboundservices.tokens.TokenService;
import com.fragbyte.iam.domain.exceptions.InvalidRefreshTokenException;
import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.commands.RefreshTokenCommand;
import com.fragbyte.iam.domain.model.entities.AccessRole;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.PasswordHash;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.infrastructure.persistence.repositories.AccessRoleRepository;
import com.fragbyte.iam.infrastructure.persistence.repositories.UserRepository;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for the refresh token use case. */
@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

  private static final String PRESENTED_TOKEN = "expired.jwt.value";
  private static final String USER_ID = "us-12345678-1234-1234-1234-123456789abc";

  @Mock private UserRepository userRepository;
  @Mock private AccessRoleRepository accessRoleRepository;
  @Mock private HashingService hashingService;
  @Mock private TokenService tokenService;
  @Mock private ExternalIdentityVerifier externalIdentityVerifier;

  private UserCommandServiceImpl service;

  @BeforeEach
  void setUp() {
    service =
        new UserCommandServiceImpl(
            userRepository,
            accessRoleRepository,
            hashingService,
            tokenService,
            externalIdentityVerifier,
            false);
  }

  @Test
  void refreshesTokenForExistingUser() {
    var user = buildActiveUser();
    when(tokenService.getUserIdFromToken(PRESENTED_TOKEN)).thenReturn(user.getUserId().value());
    when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
    when(tokenService.generateToken(
            eq(user.getEmail().email()),
            eq(user.getUserId().value()),
            eq(user.getAccessRoles())))
        .thenReturn("fresh.jwt.value");

    var result = service.handle(new RefreshTokenCommand(PRESENTED_TOKEN));

    assertThat(result).isPresent();
    assertThat(result.get().getLeft()).isEqualTo(user);
    assertThat(result.get().getRight()).isEqualTo("fresh.jwt.value");
  }

  @Test
  void throwsWhenTokenCannotBeParsed() {
    when(tokenService.getUserIdFromToken(PRESENTED_TOKEN))
        .thenThrow(new IllegalArgumentException("Invalid UserId"));

    assertThatThrownBy(() -> service.handle(new RefreshTokenCommand(PRESENTED_TOKEN)))
        .isInstanceOf(InvalidRefreshTokenException.class);
  }

  @Test
  void throwsWhenUserDoesNotExist() {
    when(tokenService.getUserIdFromToken(PRESENTED_TOKEN)).thenReturn(USER_ID);
    when(userRepository.findById(new UserId(USER_ID))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.handle(new RefreshTokenCommand(PRESENTED_TOKEN)))
        .isInstanceOf(InvalidRefreshTokenException.class);
  }

  private User buildActiveUser() {
    var role = new AccessRole(AccessRoles.USER, "Standard user of the platform");
    return User.create(
        new Email("jane@example.com"),
        new PasswordHash("hashed-password"),
        Set.of(role),
        AccountStatus.ACTIVE);
  }
}
