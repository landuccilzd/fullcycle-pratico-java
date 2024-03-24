package br.landucci.admin.catologo.application.genre.list;

import br.landucci.admin.catologo.domain.genre.Genre;

import br.landucci.admin.catologo.domain.category.CategoryID;

import java.time.Instant;
import java.util.List;

public record ListGenreOutputCommand(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {

    public static ListGenreOutputCommand from(final Genre genre) {
        var categories = genre.getCategories().stream().map(CategoryID::getValue).toList();
        return new ListGenreOutputCommand(genre.getId().getValue(), genre.getName(), genre.isActive(),
                categories, genre.getCreatedAt(), genre.getDeletedAt()
        );
    }
}
