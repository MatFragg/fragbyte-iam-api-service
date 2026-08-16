package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserAccessRoleRemovedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserAccessRoleRemovedEvent}.
 *
 * <p>Reacts after a role has been revoked from a {@code User} aggregate and persisted. Reserved for
 * triggering downstream integration, such as notifying the user through the Notifications bounded
 * context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserAccessRoleRemovedEventHandler {

  /**
   * Handles the user access role removed event.
   *
   * @param event the user access role removed event.
   */
  @TransactionalEventListener
  public void on(UserAccessRoleRemovedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
