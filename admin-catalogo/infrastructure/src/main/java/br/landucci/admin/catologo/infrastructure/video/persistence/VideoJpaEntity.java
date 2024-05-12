package br.landucci.admin.catologo.infrastructure.video.persistence;

import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.video.Rating;
import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoBuilder;
import br.landucci.admin.catologo.domain.video.VideoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "video")
@Entity(name = "Video")
public class VideoJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "year_launched", nullable = false)
    private int yearLaunched;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "rating")
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_media_id")
    private VideoMediaJpaEntity video;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_media_id")
    private VideoMediaJpaEntity trailer;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "banner_media_id")
    private ImageMediaJpaEntity banner;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_media_id")
    private ImageMediaJpaEntity thumbnail;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_half_media_id")
    private ImageMediaJpaEntity thumbnailHalf;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoGenreJpaEntity> genres;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoCastMemberJpaEntity> castMembers;

    public VideoJpaEntity() {}

    private VideoJpaEntity(final String id, final String title, final String description, final int yearLaunched,
            final boolean opened, final boolean published, final Rating rating, final double duration,
            final Instant createdAt, final Instant updatedAt, final VideoMediaJpaEntity video,
            final VideoMediaJpaEntity trailer, final ImageMediaJpaEntity banner, final ImageMediaJpaEntity thumbnail,
            final ImageMediaJpaEntity thumbnailHalf) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = new HashSet<>(3);
        this.genres = new HashSet<>(3);
        this.castMembers = new HashSet<>(3);
    }

    public static VideoJpaEntity from(final Video video) {
        final var entity = new VideoJpaEntity(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getLaunchedAt().getValue(),
                video.isOpened(),
                video.isPublished(),
                video.getRating(),
                video.getDuration(),
                video.getCreatedAt(),
                video.getUpdatedAt(),
                video.getContent().map(VideoMediaJpaEntity::from).orElse(null),
                video.getTrailer().map(VideoMediaJpaEntity::from).orElse(null),
                video.getBanner().map(ImageMediaJpaEntity::from).orElse(null),
                video.getThumbnail().map(ImageMediaJpaEntity::from).orElse(null),
                video.getThumbnailHalf().map(ImageMediaJpaEntity::from).orElse(null)
        );

        video.getCategories().forEach(entity::addCategory);
        video.getGenres().forEach(entity::addGenre);
        video.getCastMembers().forEach(entity::addCastMember);

        return entity;
    }

    public Video toAggregate() {
        return new VideoBuilder().withId(VideoID.with(getId()))
                .withTitle(getTitle())
                .withDescription(getDescription())
                .withLaunchedAt(Year.of(getYearLaunched()))
                .withDuration(getDuration())
                .withOpened(isOpened())
                .withPublished(isPublished())
                .withRating(getRating())
                .withCreatedAt(getCreatedAt())
                .withUpdatedAt(getUpdatedAt())
                .withBanner(Optional.ofNullable(getBanner())
                        .map(ImageMediaJpaEntity::toAggregate).orElse(null))
                .withThumbnail(Optional.ofNullable(getThumbnail())
                        .map(ImageMediaJpaEntity::toAggregate).orElse(null))
                .withThumbnailHalf(Optional.ofNullable(getThumbnailHalf())
                        .map(ImageMediaJpaEntity::toAggregate).orElse(null))
                .withTrailer(Optional.ofNullable(getTrailer())
                        .map(VideoMediaJpaEntity::toAggregate).orElse(null))
                .withContent(Optional.ofNullable(getVideo())
                        .map(VideoMediaJpaEntity::toAggregate).orElse(null))
                .withCategories(getCategories().stream().map(it ->
                        CategoryID.from(it.getId().getCategoryId())).collect(Collectors.toSet()))
                .withGenres(getGenres().stream().map(it ->
                        GenreID.from(it.getId().getGenreId())).collect(Collectors.toSet()))
                .withCastMembers(getCastMembers().stream().map(it ->
                        CastMemberID.with(it.getId().getCastMemberId())).collect(Collectors.toSet()))
                .build();
    }

    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public int getYearLaunched() {
        return yearLaunched;
    }
    public boolean isOpened() {
        return opened;
    }
    public boolean isPublished() {
        return published;
    }
    public Rating getRating() {
        return rating;
    }
    public double getDuration() {
        return duration;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public VideoMediaJpaEntity getVideo() {
        return video;
    }
    public VideoMediaJpaEntity getTrailer() {
        return trailer;
    }
    public ImageMediaJpaEntity getBanner() {
        return banner;
    }
    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }
    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }
    public Set<VideoCategoryJpaEntity> getCategories() {
        return categories;
    }
    public Set<VideoGenreJpaEntity> getGenres() {
        return genres;
    }
    public Set<VideoCastMemberJpaEntity> getCastMembers() {
        return castMembers;
    }

    public VideoJpaEntity setId(String id) {
        this.id = id;
        return this;
    }
    public VideoJpaEntity setTitle(String title) {
        this.title = title;
        return this;
    }
    public VideoJpaEntity setDescription(String description) {
        this.description = description;
        return this;
    }
    public VideoJpaEntity setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
        return this;
    }
    public VideoJpaEntity setOpened(boolean opened) {
        this.opened = opened;
        return this;
    }
    public VideoJpaEntity setPublished(boolean published) {
        this.published = published;
        return this;
    }
    public VideoJpaEntity setRating(Rating rating) {
        this.rating = rating;
        return this;
    }
    public VideoJpaEntity setDuration(double duration) {
        this.duration = duration;
        return this;
    }
    public VideoJpaEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    public VideoJpaEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    public VideoJpaEntity setVideo(VideoMediaJpaEntity video) {
        this.video = video;
        return this;
    }
    public VideoJpaEntity setTrailer(VideoMediaJpaEntity trailer) {
        this.trailer = trailer;
        return this;
    }
    public VideoJpaEntity setBanner(ImageMediaJpaEntity banner) {
        this.banner = banner;
        return this;
    }
    public VideoJpaEntity setThumbnail(ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
    public VideoJpaEntity setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        return this;
    }
    public VideoJpaEntity setCategories(Set<VideoCategoryJpaEntity> categories) {
        this.categories = categories;
        return this;
    }
    public VideoJpaEntity setGenres(Set<VideoGenreJpaEntity> genres) {
        this.genres = genres;
        return this;
    }
    public VideoJpaEntity setCastMembers(Set<VideoCastMemberJpaEntity> castMembers) {
        this.castMembers = castMembers;
        return this;
    }

    public void addCategory(final CategoryID anId) {
        this.categories.add(VideoCategoryJpaEntity.from(this, anId));
    }

    public void addGenre(final GenreID anId) {
        this.genres.add(VideoGenreJpaEntity.from(this, anId));
    }

    public void addCastMember(final CastMemberID anId) {
        this.castMembers.add(VideoCastMemberJpaEntity.from(this, anId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoJpaEntity that = (VideoJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return title;
    }
}
