package br.landucci.admin.catologo.application.category.create;

import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;

public record CreateCategoryOutputCommand(String id) {

    public static CreateCategoryOutputCommand from(final String id) {
        return new CreateCategoryOutputCommand(id);
    }
    public static CreateCategoryOutputCommand from(final Category category) {
        return new CreateCategoryOutputCommand(category.getId().getValue());
    }
}
