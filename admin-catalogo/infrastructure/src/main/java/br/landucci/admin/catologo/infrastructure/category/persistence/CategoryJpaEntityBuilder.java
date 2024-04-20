package br.landucci.admin.catologo.infrastructure.category.persistence;

import java.time.Instant;

public class CategoryJpaEntityBuilder {
    protected String id;
    protected String name;
    protected String description;
    protected boolean active;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;

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