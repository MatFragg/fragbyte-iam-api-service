package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserAccessRoleAssignedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserAccessRoleAssignedEvent}.
 *
 * <p>Reacts after a role has been granted to a {@code User} aggregate and persisted. Reserved for
 * triggering downstream integration, such as notifying the user through the Notifications bounded
 * context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserAccessRoleAssignedEventHandler {

  /**
   * Handles the user access role assigned event.
   *
   * @param event the user access role assigned event.
   */
  @TransactionalEventListener
  public void on(UserAccessRoleAssignedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
