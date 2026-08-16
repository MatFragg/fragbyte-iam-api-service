package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserEmailVerifiedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserEmailVerifiedEvent}.
 *
 * <p>Reacts after a {@code User} email address has been verified. Reserved for triggering
 * downstream integration, such as notifying the user through the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserEmailVerifiedEventHandler {

  /**
   * Handles the user email verified event.
   *
   * @param event the user email verified event.
   */
  @TransactionalEventListener
  public void on(UserEmailVerifiedEvent event) {
    // TODO: request the Notifications bounded context to notify the user via the
    // NotificationsFacade outbound port.
  }
}
