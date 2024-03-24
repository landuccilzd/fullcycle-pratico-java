package br.landucci.admin.catologo.infrastructure.genre.persistence;

import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private boolean active;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    public GenreJpaEntity() {}

    public GenreJpaEntity(final String id, final String name, final boolean active, final Instant createdAt,
            final Instant updatedAt, final Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.categories = new HashSet<>();
    }

    public static GenreJpaEntity from(final Genre genre) {
        var genreJpa = new GenreJpaEntity(genre.getId().getValue(), genre.getName(), genre.isActive(),
                genre.getCreatedAt(), genre.getUpdatedAt(), genre.getDeletedAt());

        genre.getCategories().forEach(genreJpa::adicionarCategoria);
        return genreJpa;
    }

    public Genre toAggregate() {
        var genre = Genre.with(GenreID.from(getId()), getName(), isActive(), getCreatedAt(), getUpdatedAt(),
                getDeletedAt());
        genre.adicionarCategorias(
                this.getCategories().stream().map(it -> CategoryID.from(it.getId().getCategoryId())).toList());
        return genre;
    }

    public String getId() {
        return id;
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
    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public GenreJpaEntity setId(String id) {
        this.id = id;
        return this;
    }
    public GenreJpaEntity setName(String name) {
        this.name = name;
        return this;
    }
    public GenreJpaEntity setActive(boolean active) {
        this.active = active;
        return this;
    }
    public GenreJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public GenreJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public GenreJpaEntity setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
    public GenreJpaEntity setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
        return this;
    }
    public void adicionarCategoria(final CategoryID id) {
        this.categories.add(GenreCategoryJpaEntity.with(id, this));
    }
    public void removerCategoria(final CategoryID id) {
        this.categories.remove(GenreCategoryJpaEntity.with(id, this));
    }
}
