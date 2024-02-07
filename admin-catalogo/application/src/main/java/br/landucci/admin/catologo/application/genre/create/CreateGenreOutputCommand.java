package br.landucci.admin.catologo.application.genre.create;

import br.landucci.admin.catologo.domain.genre.Genre;

public record CreateGenreOutputCommand(String id) {

    public static CreateGenreOutputCommand with(final String id) {
        return new CreateGenreOutputCommand(id);
    }

    public static CreateGenreOutputCommand from(final Genre genre) {
        return with(genre.getId().getValue());
    }
}
