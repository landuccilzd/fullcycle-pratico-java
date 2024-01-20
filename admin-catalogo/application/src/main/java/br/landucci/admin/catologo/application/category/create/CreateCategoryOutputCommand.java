package br.landucci.admin.catologo.application.category.create;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;

public record CreateCategoryOutputCommand(CategoryID id) {

    public static CreateCategoryOutputCommand from(final Category category) {
        return new CreateCategoryOutputCommand(category.getId());
    }
}
