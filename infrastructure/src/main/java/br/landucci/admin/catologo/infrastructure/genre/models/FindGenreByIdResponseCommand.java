package br.landucci.admin.catologo.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

public record FindGenreByIdResponseCommand(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
    public boolean isActive() {
        return this.active != null ? this.active : Boolean.TRUE;
    }

    public List<String> categories() {
        return this.categories != null ? this.categories : Collections.emptyList();
    }

    public boolean hasCategories() {
        return this.categories != null && !this.categories.isEmpty();
    }

    public int categoriesCount() {
        if (!hasCategories()) {
            return 0;
        }
        return this.categories.size();
    }
}
