package br.landucci.admin.catologo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryRequestCommand(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
    public Boolean active() {
        return this.active != null ? this.active : Boolean.FALSE;
    }
}
