package br.landucci.admin.catologo.infrastructure.genre.persistence;

import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
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

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;

    public GenreJpaEntity() {}

    protected GenreJpaEntity(GenreJpaEntityBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.active = builder.isActive();
        this.createdAt = builder.getCreatedAt();
        this.updatedAt = builder.getUpdatedAt();
        this.deletedAt = builder.getDeletedAt();
        this.categories = builder.getCategories();
    }

    public static GenreJpaEntity from(final Genre genre) {
        var builder = new GenreJpaEntityBuilder();
        var entity = builder.withId(genre.getId().getValue())
                .withName(genre.getName())
                .withActive(genre.isActive())
                .withCreatedAt(genre.getCreatedAt())
                .withUpdatedAt(genre.getUpdatedAt())
                .withDeletedAt(genre.getDeletedAt())
                .build();
        genre.getCategories().forEach(entity::adicionarCategoria);
        return entity;
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

    public List<CategoryID> getCategoriesAsList() {
        return this.categories.stream().map(it ->
                CategoryID.from(it.getId().getCategoryId())
        ).toList();
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
        this.categories.add(criarEntity(id));
    }

    public void removerCategoria(final CategoryID id) {
        this.categories.remove(criarEntity(id));
    }

    public int categoriesCount() {
        if (this.categories == null) {
            return 0;
        }
        return this.categories.size();
    }

    private GenreCategoryJpaEntity criarEntity(CategoryID id) {
        return new GenreCategoryJpaEntityBuilder()
                .withId(GenreCategoryID.with(getId(), id.getValue()))
                .withGenre(this)
                .build();
    }

}