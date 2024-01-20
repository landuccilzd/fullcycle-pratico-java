package br.landucci.admin.catologo.application.category.update;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;

public record UpdateCategoryOutputCommand(CategoryID id) {

    public static UpdateCategoryOutputCommand from(final Category category) {
        return new UpdateCategoryOutputCommand(category.getId());
    }
}
