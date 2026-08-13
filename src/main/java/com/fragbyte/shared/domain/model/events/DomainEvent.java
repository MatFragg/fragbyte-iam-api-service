package com.fragbyte.shared.domain.model.events;

import java.time.Instant;

/**
 * Base class for all domain events in the platform. This class extends Spring's ApplicationEvent
 * and adds common properties for all domain events.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface DomainEvent {
  /**
   * Returns the unique event identifier.
   *
   * @return the event ID
   */
  String eventId();

  /**
   * Returns the timestamp when the event occurred.
   *
   * @return the occurrence timestamp
   */
  Instant occurredOn();
}
