package br.landucci.admin.catologo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Set;

public record VideoResponseCommand(
        @JsonProperty("id") String id,
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("year_launched") int yearLaunched,
        @JsonProperty("duration") double duration,
        @JsonProperty("opened") boolean opened,
        @JsonProperty("published") boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("banner") ImageMediaResponseCommand banner,
        @JsonProperty("thumbnail") ImageMediaResponseCommand thumbnail,
        @JsonProperty("thumbnail_half") ImageMediaResponseCommand thumbnailHalf,
        @JsonProperty("video") VideoMediaResponseCommand video,
        @JsonProperty("trailer") VideoMediaResponseCommand trailer,
        @JsonProperty("categories_id") Set<String> categoriesId,
        @JsonProperty("genres_id") Set<String> genresId,
        @JsonProperty("cast_members_id") Set<String> castMembersId
) {
}
