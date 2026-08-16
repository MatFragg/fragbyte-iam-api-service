package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserLockedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserLockedEvent}.
 *
 * <p>Reacts after a {@code User} account has been locked. Reserved for triggering downstream
 * integration, such as notifying the user through the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserLockedEventHandler {

  /**
   * Handles the user locked event.
   *
   * @param event the user locked event.
   */
  @TransactionalEventListener
  public void on(UserLockedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
