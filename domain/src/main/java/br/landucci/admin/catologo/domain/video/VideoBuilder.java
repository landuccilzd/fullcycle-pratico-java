package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.events.DomainEvent;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.utils.InstantUtils;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Set;

public class VideoBuilder {
    protected VideoID id;
    protected String title;
    protected String description;
    protected Year launchedAt;
    protected double duration;
    protected Rating rating;
    protected boolean opened;
    protected boolean published;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected ImageMedia banner;
    protected ImageMedia thumbnail;
    protected ImageMedia thumbnailHalf;
    protected VideoMedia trailer;
    protected VideoMedia content;
    protected Set<CategoryID> categories;
    protected Set<GenreID> genres;
    protected Set<CastMemberID> castMembers;
    protected List<DomainEvent> domainEvents;

    public VideoBuilder withId(VideoID id) {
        this.id = id;
        return this;
    }
    public VideoBuilder withTitle(String title) {
        this.title = title;
        return this;
    }
    public VideoBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    public VideoBuilder withLaunchedAt(Year launchedAt) {
        this.launchedAt = launchedAt;
        return this;
    }
    public VideoBuilder withDuration(double duration) {
        this.duration = duration;
        return this;
    }
    public VideoBuilder withRating(Rating rating) {
        this.rating = rating;
        return this;
    }
    public VideoBuilder withOpened(boolean opened) {
        this.opened = opened;
        return this;
    }
    public VideoBuilder withPublished(boolean published) {
        this.published = published;
        return this;
    }
    public VideoBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public VideoBuilder withUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public VideoBuilder withBanner(ImageMedia banner) {
        this.banner = banner;
        return this;
    }
    public VideoBuilder withThumbnail(ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
    public VideoBuilder withThumbnailHalf(ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        return this;
    }
    public VideoBuilder withTrailer(VideoMedia trailer) {
        this.trailer = trailer;
        return this;
    }
    public VideoBuilder withContent(VideoMedia content) {
        this.content = content;
        return this;
    }
    public VideoBuilder withCategories(Set<CategoryID> categories) {
        this.categories = categories;
        return this;
    }
    public VideoBuilder withGenres(Set<GenreID> genres) {
        this.genres = genres;
        return this;
    }
    public VideoBuilder withCastMembers(Set<CastMemberID> castMembers) {
        this.castMembers = castMembers;
        return this;
    }
    public VideoBuilder withDomainEvents(List<DomainEvent> domainEvents) {
        this.domainEvents = domainEvents;
        return this;
    }
    public Video build() {
        final var now = InstantUtils.now();
        this.withCreatedAt(now).withUpdatedAt(now);
        return new Video(this);
    }
    public Video newVideo() {
        this.withId(VideoID.unique());
        return build();
    }
}
