package com.emirhanarici.bankapplication.model;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * A base class for entities in the Simple Banking App, providing a createdDateTime field.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public class BaseEntity {

    /**
     * The date and time the entity was created.
     */
    private LocalDateTime createdDateTime;

    /**
     * Sets the createdDateTime to the current date and time when an entity is persisted.
     */
    @PrePersist
    public void onCreate() {
        this.createdDateTime = LocalDateTime.now();
    }


}
