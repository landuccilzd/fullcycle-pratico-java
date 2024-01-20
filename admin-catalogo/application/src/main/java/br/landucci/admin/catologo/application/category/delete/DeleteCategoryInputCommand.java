package br.landucci.admin.catologo.application.category.delete;

import br.landucci.admin.catologo.domain.category.CategoryID;

public record DeleteCategoryInputCommand(String id) {

    public static DeleteCategoryInputCommand with(final String id) {
        return new DeleteCategoryInputCommand(id);
    }
}
