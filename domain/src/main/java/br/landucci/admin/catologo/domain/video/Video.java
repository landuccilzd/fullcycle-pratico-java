package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.AggregateRoot;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.*;

public class Video extends AggregateRoot<VideoID> {

    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;
    private boolean opened;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;
    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;
    private VideoMedia trailer;
    private VideoMedia content;
    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    protected Video(VideoBuilder builder) {
        super(builder.id, builder.domainEvents);
        this.title = builder.title;
        this.description = builder.description;
        this.launchedAt = builder.launchedAt;
        this.duration = builder.duration;
        this.opened = builder.opened;
        this.published = builder.published;
        this.rating = builder.rating;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.banner = builder.banner;
        this.thumbnail = builder.thumbnail;
        this.thumbnailHalf = builder.thumbnailHalf;
        this.trailer = builder.trailer;
        this.content = builder.content;
        this.categories = builder.categories;
        this.genres = builder.genres;
        this.castMembers = builder.castMembers;
    }

    public static Video clone(final Video video) {
        return new VideoBuilder().withId(video.getId())
                .withTitle(video.getTitle())
                .withDescription(video.getDescription())
                .withLaunchedAt(video.getLaunchedAt())
                .withDuration(video.getDuration())
                .withOpened(video.isOpened())
                .withPublished(video.isPublished())
                .withRating(video.getRating())
                .withCreatedAt(video.getCreatedAt())
                .withUpdatedAt(video.getUpdatedAt())
                .withBanner(video.getBanner().orElse(null))
                .withThumbnail(video.getThumbnail().orElse(null))
                .withThumbnailHalf(video.getThumbnailHalf().orElse(null))
                .withTrailer(video.getTrailer().orElse(null))
                .withContent(video.getContent().orElse(null))
                .withCategories(new HashSet<>(video.getCategories()))
                .withGenres(new HashSet<>(video.getGenres()))
                .withCastMembers(new HashSet<>(video.getCastMembers()))
                .withDomainEvents(video.getDomainEvents())
                .build();
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Year getLaunchedAt() {
        return launchedAt;
    }
    public double getDuration() {
        return duration;
    }
    public Rating getRating() {
        return rating;
    }
    public boolean isOpened() {
        return opened;
    }
    public boolean isPublished() {
        return published;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }
    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }
    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }
    public Optional<VideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }
    public Optional<VideoMedia> getContent() {
        return Optional.ofNullable(content);
    }
    public Set<CategoryID> getCategories() {
        return categories;
    }
    public Set<GenreID> getGenres() {
        return genres;
    }
    public Set<CastMemberID> getCastMembers() {
        return castMembers;
    }

    public Video updateTitle(String title) {
        this.title = title;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateDescription(String description) {
        this.description = description;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateLaunchedAt(Year launchedAt) {
        this.launchedAt = launchedAt;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateDuration(double duration) {
        this.duration = duration;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateRating(Rating rating) {
        this.rating = rating;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video open() {
        this.opened = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }
    public Video close() {
        this.opened = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video publish() {
        this.published = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }
    public Video unpublish() {
        this.published = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateBanner(ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnail(ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailHalf(ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateTrailer(VideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        this.onVideoMediaUpdated(trailer);
        return this;
    }

    public Video updateContent(VideoMedia content) {
        this.content = content;
        this.updatedAt = InstantUtils.now();
        this.onVideoMediaUpdated(content);
        return this;
    }

    public Video updateCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
        this.updatedAt = InstantUtils.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public Video process(final VideoMediaType type) {
        if (VideoMediaType.VIDEO == type) {
            getContent().ifPresent(media -> updateContent(media.process()));
        } else if (VideoMediaType.TRAILER == type) {
            getTrailer().ifPresent(media -> updateTrailer(media.process()));
        }

        return this;
    }

    public Video complete(final VideoMediaType type, final String encodedPath) {
        if (VideoMediaType.VIDEO == type) {
            getContent().ifPresent(media -> updateContent(media.complete(encodedPath)));
        } else if (VideoMediaType.TRAILER == type) {
            getTrailer().ifPresent(media -> updateTrailer(media.complete(encodedPath)));
        }

        return this;
    }

    private void onVideoMediaUpdated(final VideoMedia media) {
        if (media != null && media.isPendingEncode()) {
            this.registerEvent(new VideoMediaCreated(getId().getValue(), media.getRawLocation()));
        }
    }
}
