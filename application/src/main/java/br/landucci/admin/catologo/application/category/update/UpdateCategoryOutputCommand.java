package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.domain.category.Category;

public record UpdateCategoryOutputCommand(String id) {

    public static UpdateCategoryOutputCommand from(final String id) {
        return new UpdateCategoryOutputCommand(id);
    }

    public static UpdateCategoryOutputCommand from(final Category category) {
        return new UpdateCategoryOutputCommand(category.getId().getValue());
    }
}
