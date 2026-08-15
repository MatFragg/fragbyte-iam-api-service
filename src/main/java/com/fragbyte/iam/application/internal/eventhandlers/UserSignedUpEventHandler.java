package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserSignedUpEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles the {@link UserSignedUpEvent}.
 *
 * <p>Reacts after the {@code User} aggregate is persisted. Reserved for triggering downstream
 * integration, such as requesting a verification email from the Notifications bounded context.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service
public class UserSignedUpEventHandler {

  /**
   * Handles the user signed up event.
   *
   * @param event the user signed up event.
   */
  @TransactionalEventListener
  public void on(UserSignedUpEvent event) {
    // TODO: request the Notifications bounded context to send a verification email via the
    // NotificationsFacade outbound port.
  }
}
