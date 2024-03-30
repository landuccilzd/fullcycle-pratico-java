package br.landucci.admin.catologo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record UpdateGenreRequestCommand(
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories
) {
    public boolean isActive() {
        return this.active != null ? this.active : true;
    }

    public List<String> categories() {
        return this.categories != null ? this.categories : Collections.emptyList();
    }

    public boolean hasCategories() {
        return this.categories != null && this.categories.size() > 0;
    }

    public int categoriesCount() {
        if (!hasCategories()) {
            return 0;
        }
        return this.categories.size();
    }
}
