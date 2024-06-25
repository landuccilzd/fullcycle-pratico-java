package br.landucci.admin.catologo.application.genre.delete;
public record DeleteGenreInputCommand(String id) {
    public static DeleteGenreInputCommand with(final String id) {
        return new DeleteGenreInputCommand(id);
    }
}
