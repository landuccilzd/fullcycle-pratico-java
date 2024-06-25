package br.landucci.admin.catologo.application.genre.update;

import java.util.List;

public record UpdateGenreInputCommand(String id, String name, boolean active, List<String> categories) {

    public static UpdateGenreInputCommand with(final String id, final String name, final boolean active,
                                               final List<String> categories) {
        return new UpdateGenreInputCommand(id, name, active, categories);
    }

}