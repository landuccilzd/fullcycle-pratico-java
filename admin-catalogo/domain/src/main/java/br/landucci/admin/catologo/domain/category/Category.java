package br.landucci.admin.catologo.domain.category;

import br.landucci.admin.catologo.domain.AggregateRoot;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id, final String name, final String description, final boolean active,
            final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.active = Objects.requireNonNull(active, "Active should not be null");
        this.createdAt = Objects.requireNonNull(createdAt,"Created At should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated At should not be null");
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final boolean active) {
        final var id = CategoryID.unique();
        final var now  = Instant.now();
        final var nowDeleted  = active ? null : now;
        return with(id, name, description, active, now, now, nowDeleted);
    }

    public static Category with(final CategoryID id, final String name, final String description, final boolean active,
                                final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        return new Category(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public CategoryID getId() {
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

    public Category updateName(final String name) {
        this.name = name;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category updateDescription(final String description) {
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (this.deletedAt == null) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}