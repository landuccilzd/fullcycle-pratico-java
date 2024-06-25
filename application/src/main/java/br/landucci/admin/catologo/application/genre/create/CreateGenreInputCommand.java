package br.landucci.admin.catologo.application.genre.create;

import java.util.List;

public record CreateGenreInputCommand(String name, boolean active, List<String> categories) {

    public static CreateGenreInputCommand with(final String name, final boolean active, final List<String> categories) {
        return new CreateGenreInputCommand(name, active, categories);
    }

}