package com.fragbyte.iam.infrastructure.bootstrap;

import com.fragbyte.iam.domain.model.commands.SeedRolesCommand;
import com.fragbyte.iam.domain.services.RoleCommandService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the canonical platform access roles at application startup.
 *
 * <p>Delegates to the {@link RoleCommandService} so the seeding logic lives in the application
 * layer while the bootstrap wiring stays in infrastructure.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Component
public class AccessRoleSeedInitializer implements ApplicationRunner {

  private final RoleCommandService roleCommandService;

  /**
   * Creates the seed initializer.
   *
   * @param roleCommandService the role command service
   */
  public AccessRoleSeedInitializer(RoleCommandService roleCommandService) {
    this.roleCommandService = roleCommandService;
  }

  /** {@inheritDoc} */
  @Override
  public void run(ApplicationArguments args) {
    roleCommandService.handle(new SeedRolesCommand());
  }
}
