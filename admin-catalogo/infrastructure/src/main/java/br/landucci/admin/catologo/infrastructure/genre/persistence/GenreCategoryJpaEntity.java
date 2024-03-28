package br.landucci.admin.catologo.infrastructure.genre.persistence;

import br.landucci.admin.catologo.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "genre_category")
public class GenreCategoryJpaEntity {
    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {}

    protected GenreCategoryJpaEntity(GenreCategoryJpaEntityBuilder builder) {
        var categoryID = CategoryID.from(builder.getId().getCategoryId());
        this.id = GenreCategoryID.with(builder.getGenre().getId(), categoryID.getValue());
        this.genre = builder.getGenre();
    }

    public GenreCategoryID getId() {
        return id;
    }
    public GenreJpaEntity getGenre() {
        return genre;
    }

    public GenreCategoryJpaEntity setId(GenreCategoryID id) {
        this.id = id;
        return this;
    }
    public GenreCategoryJpaEntity setGenre(GenreJpaEntity genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
