package br.landucci.admin.catologo.application.category.delete;


public record DeleteCategoryInputCommand(String id) {

    public static DeleteCategoryInputCommand with(final String id) {
        return new DeleteCategoryInputCommand(id);
    }
}
