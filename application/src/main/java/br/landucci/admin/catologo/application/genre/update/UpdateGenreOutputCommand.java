package br.landucci.admin.catologo.application.genre.update;

import br.landucci.admin.catologo.domain.genre.Genre;

public record UpdateGenreOutputCommand(String id) {
    public static UpdateGenreOutputCommand with(final String id) {
        return new UpdateGenreOutputCommand(id);
    }
    public static UpdateGenreOutputCommand from(final Genre genre) {
        return with(genre.getId().getValue());
    }
}
