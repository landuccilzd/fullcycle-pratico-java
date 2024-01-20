package br.landucci.admin.catologo.application.category.find;

public record FindByIDCategoryInputCommand(String id) {

    public static FindByIDCategoryInputCommand with(final String id) {
        return new FindByIDCategoryInputCommand(id);
    }
}
