package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserDisabledEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserDisabledEvent}.
 *
 * <p>Reacts after a {@code User} account has been disabled. Reserved for triggering downstream
 * integration, such as notifying the user through the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserDisabledEventHandler {

  /**
   * Handles the user disabled event.
   *
   * @param event the user disabled event.
   */
  @TransactionalEventListener
  public void on(UserDisabledEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
