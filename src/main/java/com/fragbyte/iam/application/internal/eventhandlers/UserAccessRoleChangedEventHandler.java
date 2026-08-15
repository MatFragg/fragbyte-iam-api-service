package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserAccessRoleChangedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserAccessRoleChangedEvent}.
 *
 * <p>Reacts after the {@code User} aggregate access role change is persisted. Reserved for
 * triggering downstream integration, such as notifying the user through the Notifications bounded
 * context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserAccessRoleChangedEventHandler {

  /**
   * Handles the user access role changed event.
   *
   * @param event the user access role changed event.
   */
  @TransactionalEventListener
  public void on(UserAccessRoleChangedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
