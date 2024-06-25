package br.landucci.admin.catologo.application.category.update;

public record UpdateCategoryInputCommand(String id, String name, String description, boolean active) {

    public static UpdateCategoryInputCommand with(final String id, final String name, final String description, final boolean active) {
        return new UpdateCategoryInputCommand(id, name, description, active);
    }
}
