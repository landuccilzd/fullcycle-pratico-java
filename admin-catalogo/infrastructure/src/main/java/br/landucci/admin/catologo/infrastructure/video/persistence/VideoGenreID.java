package br.landucci.admin.catologo.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoGenreID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    public VideoGenreID() {}

    private VideoGenreID(final String videoId, final String genreId) {
        this.videoId = videoId;
        this.genreId = genreId;
    }

    public static VideoGenreID from(final String videoId, final String genreId) {
        return new VideoGenreID(videoId, genreId);
    }

    public String getVideoId() {
        return videoId;
    }
    public String getGenreId() {
        return genreId;
    }

    public VideoGenreID setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }
    public VideoGenreID setGenreId(String genreId) {
        this.genreId = genreId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreID that = (VideoGenreID) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(genreId, that.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, genreId);
    }

    @Override
    public String toString() {
        return "[%s|%s]".formatted(getVideoId(), getGenreId());
    }
}
