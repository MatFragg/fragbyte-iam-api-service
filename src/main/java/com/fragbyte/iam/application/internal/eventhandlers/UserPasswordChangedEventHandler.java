package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserPasswordChangedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserPasswordChangedEvent}.
 *
 * <p>Reacts after the {@code User} aggregate password change is persisted. Reserved for triggering
 * downstream integration, such as notifying the user through the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserPasswordChangedEventHandler {

  /**
   * Handles the user password changed event.
   *
   * @param event the user password changed event.
   */
  @TransactionalEventListener
  public void on(UserPasswordChangedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
