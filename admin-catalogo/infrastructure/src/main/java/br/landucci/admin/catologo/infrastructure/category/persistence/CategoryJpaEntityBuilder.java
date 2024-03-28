package br.landucci.admin.catologo.infrastructure.category.persistence;

import java.time.Instant;

public class CategoryJpaEntityBuilder {
    private String id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public CategoryJpaEntityBuilder() {}

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public boolean isActive() {
        return active;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public Instant getDeletedAt() {
        return deletedAt;
    }

    public CategoryJpaEntityBuilder withId(String id) {
        this.id = id;
        return this;
    }
    public CategoryJpaEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public CategoryJpaEntityBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    public CategoryJpaEntityBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }
    public CategoryJpaEntityBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public CategoryJpaEntityBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public CategoryJpaEntityBuilder withDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public CategoryJpaEntity build() {
        return new CategoryJpaEntity(this);
    }
}