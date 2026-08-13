package com.fragbyte.shared.domain.model.aggregates;

import com.fragbyte.shared.domain.model.events.DomainEvent;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Base aggregate root with automatic auditing support.
 *
 * <p>Provides {@code createdAt} and {@code updatedAt} timestamps managed by Spring Data JPA
 * auditing, along with domain event registration via {@link AbstractAggregateRoot}. <hr>
 *
 * @param <T> the concrete aggregate root type (self-referential)
 * @author FragByte Development team.
 * @since 2026-13-08
 * @see AbstractAggregateRoot
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class AuditableAbstractAggregateRoot<T extends AbstractAggregateRoot<T>>
  extends AbstractAggregateRoot<T> {

  /**
   * Timestamp when the aggregate was first persisted. Automatically populated by {@link
   * CreatedDate}.
   */
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Date createdAt;

  /**
   * Timestamp of the most recent update to the aggregate. Automatically populated by {@link
   * LastModifiedDate}.
   */
  @LastModifiedDate
  @Column(nullable = false)
  private Date updatedAt;

  /**
   * Template method for subclasses to enforce invariant validation. Called before persistence
   * operations.
   */
  protected void validateInvariants() {}

  /**
   * Registers a domain event to be published after the aggregate is saved.
   *
   * @param event the domain event to publish
   */
  protected void publishEvent(DomainEvent event) {
    registerEvent(event);
  }
}