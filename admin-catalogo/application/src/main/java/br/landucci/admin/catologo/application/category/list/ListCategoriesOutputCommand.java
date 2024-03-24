package br.landucci.admin.catologo.application.category.list;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;

import java.time.Instant;

public record ListCategoriesOutputCommand(CategoryID id, String name, String description, boolean active,
        Instant createdAt, Instant updatedAt, Instant deletedAt) {

    public static ListCategoriesOutputCommand from(final Category category) {
        return new ListCategoriesOutputCommand(category.getId(), category.getName(), category.getDescription(),
                category.isActive(), category.getCreatedAt(), category.getUpdatedAt(), category.getDeletedAt());
    }
}
