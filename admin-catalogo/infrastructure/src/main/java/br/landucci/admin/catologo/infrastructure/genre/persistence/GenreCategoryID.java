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

    protected GenreCategoryID(GenreCategoryIDBuilder builder) {
        this.genreId = builder.getGenreId();
        this.categoryId = builder.getCategoryId();
    }

    public static GenreCategoryID with(final String genreId, final String categoryId) {
        return new GenreCategoryIDBuilder().withGenreId(genreId).withCategoryId(categoryId).build();
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

}