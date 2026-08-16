package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserProvisionedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserProvisionedEvent}.
 *
 * <p>Reacts after a {@code User} has been provisioned by an administrator and persisted. Reserved
 * for triggering downstream integration, such as notifying the user through the Notifications
 * bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserProvisionedEventHandler {

  /**
   * Handles the user provisioned event.
   *
   * @param event the user provisioned event.
   */
  @TransactionalEventListener
  public void on(UserProvisionedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
