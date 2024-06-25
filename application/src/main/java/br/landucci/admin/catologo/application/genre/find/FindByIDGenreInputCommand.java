package br.landucci.admin.catologo.application.genre.find;

public record FindByIDGenreInputCommand(String id) {

    public static FindByIDGenreInputCommand with(final String id) {
        return new FindByIDGenreInputCommand(id);
    }
}
