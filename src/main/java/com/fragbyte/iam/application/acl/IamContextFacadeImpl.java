package com.fragbyte.iam.application.acl;

import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.queries.GetUserByEmailQuery;
import com.fragbyte.iam.domain.model.queries.GetUserByIdQuery;
import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.domain.model.valueobjects.UserId;
import com.fragbyte.iam.domain.services.UserQueryService;
import com.fragbyte.iam.interfaces.acl.IamContextFacade;
import com.fragbyte.iam.interfaces.acl.UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Default implementation of {@link IamContextFacade}.
 *
 * <p>Adapts the IAM {@link UserQueryService} into the anti-corruption layer contract consumed by
 * other bounded contexts.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
@Transactional(readOnly = true)
public class IamContextFacadeImpl implements IamContextFacade {

  private final UserQueryService userQueryService;

  /**
   * Creates the facade implementation.
   *
   * @param userQueryService the IAM user query service
   */
  public IamContextFacadeImpl(UserQueryService userQueryService) {
    this.userQueryService = userQueryService;
  }

  /** {@inheritDoc} */
  @Override
  public Optional<UserInfo> getUserByUserId(UserId userId) {
    return userQueryService.handle(new GetUserByIdQuery(userId)).map(this::toUserInfo);
  }

  /** {@inheritDoc} */
  @Override
  public Optional<UserInfo> getUserByEmail(Email email) {
    return userQueryService.handle(new GetUserByEmailQuery(email)).map(this::toUserInfo);
  }

  private UserInfo toUserInfo(User user) {
    return new UserInfo(
        user.getUserId(), user.getEmail(), user.getAccessRoles(), user.getAccountStatus());
  }
}
