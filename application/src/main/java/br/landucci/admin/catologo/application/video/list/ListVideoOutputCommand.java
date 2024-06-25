package br.landucci.admin.catologo.application.video.list;

import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoPreview;

import java.time.Instant;

public record ListVideoOutputCommand(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {

    public static ListVideoOutputCommand from(final Video video) {
        return new ListVideoOutputCommand(
                video.getId().getValue(),
                video.getTitle(),
                video.getDescription(),
                video.getCreatedAt(),
                video.getUpdatedAt()
        );
    }

    public static ListVideoOutputCommand from(final VideoPreview videoPreview) {
        return new ListVideoOutputCommand(
                videoPreview.id(),
                videoPreview.title(),
                videoPreview.description(),
                videoPreview.createdAt(),
                videoPreview.updatedAt()
        );
    }
}
