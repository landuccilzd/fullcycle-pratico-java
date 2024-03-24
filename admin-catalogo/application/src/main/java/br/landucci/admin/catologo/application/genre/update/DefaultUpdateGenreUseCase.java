package br.landucci.admin.catologo.application.genre.update;

import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.category.CategoryGateway;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final GenreGateway gateway;
    private final CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(final GenreGateway gateway, final CategoryGateway categoryGateway) {
        this.gateway = Objects.requireNonNull(gateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateGenreOutputCommand execute(final UpdateGenreInputCommand input) {
        final var id = GenreID.from(input.id());
        final var name = input.name();
        final var active = input.active();
        final var categories = asCategoryID(input.categories());

        final var genre = this.gateway.findById(id).orElseThrow(notFound(id));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> {
            genre.updateName(name);

            if (active ) {
                genre.activate();
            } else {
                genre.deactivate();
            }

            return genre;
        });

        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Genre", notification);
        }

        final var output = this.gateway.update(genre);
        return UpdateGenreOutputCommand.from(output);
    }

    private static Supplier<NotFoundException> notFound(final Identifier id) {
        return () -> NotFoundException.with(Genre.class, id);
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retriviedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retriviedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retriviedIds);

            final var missingIdsMessage = missingIds.stream().map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new ValidationError("Some categories were not found: %s".formatted(missingIdsMessage)));

        }

        return notification;
    }


    private List<CategoryID> asCategoryID(final List<String> ids) {
        return ids.stream().map(CategoryID::from).toList();
    }
}
