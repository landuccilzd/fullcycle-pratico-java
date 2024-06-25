package br.landucci.admin.catologo.infrastructure.genre.persistence;

public class GenreCategoryIDBuilder {
    private String genreId;
    private String categoryId;

    public String getGenreId() {
        return genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

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
