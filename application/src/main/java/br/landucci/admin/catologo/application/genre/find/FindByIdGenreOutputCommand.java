package br.landucci.admin.catologo.application.genre.find;

import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.category.CategoryID;

import java.time.Instant;
import java.util.List;

public record FindByIdGenreOutputCommand(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static FindByIdGenreOutputCommand from(final Genre genre) {
        final var categorias = genre.getCategories().stream().map(CategoryID::getValue).toList();
        return new FindByIdGenreOutputCommand(genre.getId().getValue(), genre.getName(),  genre.isActive(),
                categorias, genre.getCreatedAt(), genre.getUpdatedAt(), genre.getDeletedAt()
        );
    }

    public int categoriesCount() {
        if (categories == null || categories.isEmpty()) {
            return 0;
        }
        return categories.size();
    }
}
