package com.fragbyte.iam.application.internal.commandservices;

import com.fragbyte.iam.domain.model.commands.SeedRolesCommand;
import com.fragbyte.iam.domain.model.entities.AccessRole;
import com.fragbyte.iam.domain.model.valueobjects.AccessRoles;
import com.fragbyte.iam.domain.services.RoleCommandService;
import com.fragbyte.iam.infrastructure.persistence.repositories.AccessRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/** Role command service implementation. {@inheritDoc}
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
@Transactional
public class RoleCommandServiceImpl implements RoleCommandService {
  private final AccessRoleRepository accessRoleRepository;

  /**
   * Role command service constructor.
   *
   * @param accessRoleRepository the access role repository
   */
  public RoleCommandServiceImpl(AccessRoleRepository accessRoleRepository) {
    this.accessRoleRepository = accessRoleRepository;
  }

  /** {@inheritDoc} */
  @Override
  public void handle(SeedRolesCommand command) {
    Arrays.stream(AccessRoles.values())
        .forEach(
            name -> {
              if (!accessRoleRepository.existsByName(name)) {
                accessRoleRepository.save(new AccessRole(name, defaultDescriptionFor(name)));
              }
            });
  }

  private String defaultDescriptionFor(AccessRoles name) {
    return switch (name) {
      case USER -> "Standard user of the platform";
      case ADMIN -> "Administrator with elevated management capabilities";
      case SUPERADMIN -> "Super administrator with full platform control";
      case SUPPORT -> "Support agent with support tool access";
    };
  }
}
