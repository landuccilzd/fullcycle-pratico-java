package br.landucci.admin.catologo.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCategoryID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public VideoCategoryID() {}

    private VideoCategoryID(final String videoId, final String categoryId) {
        this.videoId = videoId;
        this.categoryId = categoryId;
    }

    public static VideoCategoryID from(final String videoId, final String categoryId) {
        return new VideoCategoryID(videoId, categoryId);
    }

    public String getVideoId() {
        return videoId;
    }
    public String getCategoryId() {
        return categoryId;
    }

    public VideoCategoryID setVideoId(String videoId) {
        this.videoId = videoId;
        return this;
    }
    public VideoCategoryID setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryID that = (VideoCategoryID) o;
        return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(getCategoryId(), that.getCategoryId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getCategoryId());
    }

    @Override
    public String toString() {
        return "[%s|%s]".formatted(getVideoId(), getCategoryId());
    }
}
