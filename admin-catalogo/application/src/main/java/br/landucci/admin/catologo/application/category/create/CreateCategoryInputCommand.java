package br.landucci.admin.catologo.application.category.create;

public record CreateCategoryInputCommand(String name, String description, boolean active) {

    public static CreateCategoryInputCommand with(final String name, final String description, final boolean active) {
        return new CreateCategoryInputCommand(name, description, active);
    }
}
