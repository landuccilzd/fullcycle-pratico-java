package br.landucci.admin.catologo.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {
    @Column(name = "genre_id", nullable = false)
    private String genreId;
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    private GenreCategoryID() {}

    public GenreCategoryID(GenreCategoryIDBuilder builder) {
        this.genreId = builder.genreId;
        this.categoryId = builder.categoryId;
    }
    private GenreCategoryID(final String genreId, final String categoryId) {
        this.genreId = genreId;
        this.categoryId = categoryId;
    }

    public static GenreCategoryID with(final String genreId, final String categoryId) {
        var builder = new GenreCategoryIDBuilder();
        builder.withGenreId(genreId);
        builder.withCategoryId(categoryId);
        return builder.build();
    }

    public String getGenreId() {
        return genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(genreId, that.genreId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, categoryId);
    }

    @Override
    public String toString() {
        return genreId + " - " + categoryId;
    }

    public static class GenreCategoryIDBuilder {
        private String genreId;
        private String categoryId;

        public GenreCategoryIDBuilder withGenreId(String genreId) {
            this.genreId = genreId;
            return this;
        }

        public GenreCategoryIDBuilder withCategoryId(String categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public GenreCategoryID build() {
            return new GenreCategoryID(this);
        }
    }
}
