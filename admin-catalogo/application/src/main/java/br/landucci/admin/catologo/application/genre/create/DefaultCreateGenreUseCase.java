package br.landucci.admin.catologo.application.genre.create;

import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private final GenreGateway gateway;
    private final CategoryGateway categoryGateway;


    public DefaultCreateGenreUseCase(final GenreGateway gateway, final CategoryGateway categoryGateway) {
        this.gateway = Objects.requireNonNull(gateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutputCommand execute(final CreateGenreInputCommand input) {
        final var name = input.name();
        final var active = input.active();
        final var categories = asCategoryID(input.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        final var genre = notification.validate(() -> Genre.newGenre(name, active));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }

        genre.adicionarCategorias(categories);
        final var output = this.gateway.create(genre);
        return CreateGenreOutputCommand.from(output);
    }

    private ValidationHandler validateCategories(final List<CategoryID> categories) {
        final var notification = Notification.create();
        if (categories == null || categories.isEmpty()) {
            return notification;
        }

        final var retriviedIds = categoryGateway.existsByIds(categories);
        if (categories.size() != retriviedIds.size()) {
            final var ids = new ArrayList<>(categories);
            ids.removeAll(retriviedIds);

            final var missingIds = ids.stream().map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new ValidationError("Some categories were not found: %s".formatted(missingIds)));

        }

        return notification;
    }


    private List<CategoryID> asCategoryID(final List<String> ids) {
        return ids.stream().map(CategoryID::from).toList();
    }
}
