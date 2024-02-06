package br.landucci.admin.catologo.domain.genre;

import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {

    private String value;

    private GenreID(String value) {
        this.value = value;
    }

    public static GenreID from(final String id) {
        return new GenreID(id);
    }

    public static GenreID from(final UUID id) {
        return from(id.toString().toLowerCase());
    }

    public static GenreID unique() {
        return from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreID genreID = (GenreID) o;
        return Objects.equals(getValue(), genreID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
