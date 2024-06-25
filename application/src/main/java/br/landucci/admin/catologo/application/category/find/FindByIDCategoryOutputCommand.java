package br.landucci.admin.catologo.application.category.find;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;

import java.time.Instant;

public record FindByIDCategoryOutputCommand(CategoryID id, String name, String description, boolean active,
                                            Instant createdAt, Instant updatedAt, Instant deletedAt) {

    public static FindByIDCategoryOutputCommand from(final CategoryID id, final String name, final String description,
                                                     final boolean active, final Instant createdAt,
                                                     final Instant updatedAt, final Instant deletedAt) {
        return new FindByIDCategoryOutputCommand(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    public static FindByIDCategoryOutputCommand from(Category category) {
        return new FindByIDCategoryOutputCommand(category.getId(), category.getName(), category.getDescription(),
                category.isActive(), category.getCreatedAt(), category.getUpdatedAt(), category.getDeletedAt());
    }
}
