package com.ffreaky.utilities.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {
    @Column(name = "created_at", updatable = false, insertable = false, nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", updatable = false, insertable = false, nullable = false)
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at", insertable = false)
    private LocalDateTime deletedAt;
}
