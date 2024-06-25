package br.landucci.admin.catologo.infrastructure.category.persistence;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "category")
public class CategoryJpaEntity {
    @Id
    private String id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "description", length = 4000)
    private String description;
    @Column(name = "active", nullable = false)
    private boolean active;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public CategoryJpaEntity() {}

    protected CategoryJpaEntity(final CategoryJpaEntityBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.deletedAt = builder.deletedAt;
    }

    public static CategoryJpaEntity from(final Category category) {
        return new CategoryJpaEntityBuilder().withId(category.getId().getValue())
                .withName(category.getName()).withDescription(category.getDescription())
                .withActive(category.isActive()).withCreatedAt(category.getCreatedAt())
                .withUpdatedAt(category.getUpdatedAt()).withDeletedAt(category.getDeletedAt()).build();
    }

    public Category toAggregate() {
        return Category.with(CategoryID.from(getId()), getName(), getDescription(), isActive(), getCreatedAt(),
                getUpdatedAt(), getDeletedAt());
    }

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

    public CategoryJpaEntity setId(String id) {
        this.id = id;
        return this;
    }
    public CategoryJpaEntity setName(String name) {
        this.name = name;
        return this;
    }
    public CategoryJpaEntity setDescription(String description) {
        this.description = description;
        return this;
    }
    public CategoryJpaEntity setActive(boolean active) {
        this.active = active;
        return this;
    }
    public CategoryJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public CategoryJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public CategoryJpaEntity setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryJpaEntity that = (CategoryJpaEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return name;
    }
}