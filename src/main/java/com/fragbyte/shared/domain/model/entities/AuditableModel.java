package com.fragbyte.shared.domain.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Base entity class providing automatic auditing timestamps.
 *
 * <p>Subclasses automatically receive {@code createdAt} and {@code updatedAt} fields managed by
 * Spring Data JPA auditing.
 *
 * @see CreatedDate
 * @see LastModifiedDate
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditableModel {

  /**
   * Timestamp when the aggregate was first persisted. Automatically populated by {@link
   * CreatedDate}.
   */
  @Getter
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Date createdAt;

  /**
   * Timestamp of the most recent update to the aggregate. Automatically populated by {@link
   * LastModifiedDate}.
   */
  @Getter
  @LastModifiedDate
  @Column(nullable = false)
  private Date updatedAt;
}