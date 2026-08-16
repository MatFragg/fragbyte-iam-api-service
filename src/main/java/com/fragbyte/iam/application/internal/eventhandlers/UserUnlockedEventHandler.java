package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserUnlockedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserUnlockedEvent}.
 *
 * <p>Reacts after a {@code User} account has been unlocked. Reserved for triggering downstream
 * integration, such as notifying the user through the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserUnlockedEventHandler {

  /**
   * Handles the user unlocked event.
   *
   * @param event the user unlocked event.
   */
  @TransactionalEventListener
  public void on(UserUnlockedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
