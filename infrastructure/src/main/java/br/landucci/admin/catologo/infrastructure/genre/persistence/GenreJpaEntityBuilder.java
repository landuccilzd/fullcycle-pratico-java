package br.landucci.admin.catologo.infrastructure.genre.persistence;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class GenreJpaEntityBuilder {
    private String id;
    private String name;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private Set<GenreCategoryJpaEntity> categories = new HashSet<>();

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
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
    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public GenreJpaEntityBuilder withId(String id) {
        this.id = id;
        return this;
    }
    public GenreJpaEntityBuilder withName(String name) {
        this.name = name;
        return this;
    }
    public GenreJpaEntityBuilder withActive(boolean active) {
        this.active = active;
        return this;
    }
    public GenreJpaEntityBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public GenreJpaEntityBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public GenreJpaEntityBuilder withDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
    public GenreJpaEntityBuilder withCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
        return this;
    }

    public GenreJpaEntity build() {
        return new GenreJpaEntity(this);
    }

}