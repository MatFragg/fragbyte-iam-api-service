package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserEnabledEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * User enabled event handler.
 *
 * <p>Handles the {@link UserEnabledEvent} by dispatching downstream integrations (e.g. notification
 * to the user that their account has been re-enabled).
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Service
public class UserEnabledEventHandler {

  /**
   * Handles the user enabled event.
   *
   * @param event the user enabled event
   */
  @TransactionalEventListener
  public void on(UserEnabledEvent event) {
    // TODO: request the Notifications bounded context
  }
}
