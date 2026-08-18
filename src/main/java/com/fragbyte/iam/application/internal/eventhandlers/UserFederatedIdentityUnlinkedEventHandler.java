package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserFederatedIdentityUnlinkedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * User federated identity unlinked event handler.
 *
 * <p>Handles the {@link UserFederatedIdentityUnlinkedEvent} by dispatching downstream integrations.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Service
public class UserFederatedIdentityUnlinkedEventHandler {

  /**
   * Handles the user federated identity unlinked event.
   *
   * @param event the user federated identity unlinked event
   */
  @TransactionalEventListener
  public void on(UserFederatedIdentityUnlinkedEvent event) {
    // TODO: request the Notifications bounded context
  }
}
