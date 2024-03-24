package br.landucci.admin.catologo.infrastructure.genre.persistence;

public class GenreCategoryJpaEntityBuilder {

    private GenreCategoryID id;
    private GenreJpaEntity genre;

    public GenreCategoryID getId() {
        return id;
    }
    public GenreJpaEntity getGenre() {
        return genre;
    }

    public GenreCategoryJpaEntityBuilder withId(GenreCategoryID id) {
        this.id = id;
        return this;
    }
    public GenreCategoryJpaEntityBuilder withGenre(GenreJpaEntity genre) {
        this.genre = genre;
        return this;
    }

    public GenreCategoryJpaEntity build() {
        return new GenreCategoryJpaEntity(this);
    }
}
