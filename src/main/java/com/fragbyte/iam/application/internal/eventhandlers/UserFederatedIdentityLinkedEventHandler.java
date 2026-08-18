package com.fragbyte.iam.application.internal.eventhandlers;

import com.fragbyte.iam.domain.model.events.UserFederatedIdentityLinkedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * User federated identity linked event handler.
 *
 * <p>Handles the {@link UserFederatedIdentityLinkedEvent} by dispatching downstream integrations.
 *
 * @author FragByte Development team.
 * @since 2026-17-08
 */
@Service
public class UserFederatedIdentityLinkedEventHandler {

  /**
   * Handles the user federated identity linked event.
   *
   * @param event the user federated identity linked event
   */
  @TransactionalEventListener
  public void on(UserFederatedIdentityLinkedEvent event) {
    // TODO: request the Notifications bounded context
  }
}
