package br.landucci.admin.catologo.domain.genre;

import br.landucci.admin.catologo.domain.AggregateRoot;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.*;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private final List<CategoryID> categories;

    private Genre(final GenreID genreID, final String name, final boolean active, final Instant createdAt,
            final Instant updatedAt, final Instant deletedAt) {
        super(genreID);
        this.name = name;
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt,"Created At should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated At should not be null");
        this.deletedAt = deletedAt;
        this.categories = new ArrayList<>();

        selfValidate();
    }

    public static Genre with(final GenreID id, final String name, final boolean active, final Instant createdAt,
            final Instant updatedAt, final Instant deletedAt) {
        return new Genre(id, name, active, createdAt, updatedAt, deletedAt);
    }

    public static Genre newGenre(final String name, final boolean active) {
        final var id = GenreID.unique();
        final var now  = InstantUtils.now();
        final var nowDeleted  = active ? null : now;
        return with(id, name, active, now, now, nowDeleted);
    }

    public static Genre clone(Genre genre) {
        return with(genre.getId(), genre.getName(), genre.isActive(), genre.getCreatedAt(),
                genre.getUpdatedAt(), genre.getDeletedAt());
    }

    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public Instant getDeletedAt() {
        return deletedAt;
    }
    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }
    public boolean hasCategories() {
        return this.categories != null && !this.categories.isEmpty();
    }
    public int categoriesCount() {
        return hasCategories() ? this.categories.size() : 0;
    }

    public Genre updateName(String name) {
        this.name = name;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre adicionarCategoria(CategoryID id) {
        verificarDuplicidadeCategoria(id);
        this.categories.add(id);
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre adicionarCategorias(List<CategoryID> ids) {
        if (ids == null || ids.isEmpty()) {
            return this;
        }

        for (CategoryID id: ids) {
            verificarDuplicidadeCategoria(id);
        }

        this.categories.addAll(ids);
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre removerCategoria(CategoryID id) {
        verificarInexistenciaCategoria(id);
        this.categories.remove(id);
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre removerCategorias(List<CategoryID> ids) {
        for (CategoryID id: ids) {
            verificarInexistenciaCategoria(id);
        }

        this.categories.removeAll(ids);
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre limparCategorias() {
        this.categories.clear();
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre deactivate() {
        if (this.deletedAt == null) {
            this.deletedAt = InstantUtils.now();
        }

        this.active = false;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("An validation error has ocurred: ", notification);
        }
    }

    private void verificarDuplicidadeCategoria(CategoryID id) {
        if (this.categories.contains(id)) {
            final var message = "Category with ID %s already exists".formatted(id.getValue());
            throw DomainException.with(new ValidationError(message));
        }
    }

    private void verificarInexistenciaCategoria(CategoryID id) {
        if (!this.categories.contains(id)) {
            final var message = "Category with ID %s does not exists".formatted(id.getValue());
            throw DomainException.with(new ValidationError(message));
        }
    }

    @Override
    public void validate(ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

}