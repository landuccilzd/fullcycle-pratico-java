package br.landucci.admin.catologo.application.video.media.update;

import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.*;

import java.util.Objects;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {

    private final VideoGateway gateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final UpdateMediaStatusInputCommand command) {
        final var id = VideoID.with(command.videoId());
        final var resourceId = command.resourceId();
        final var folder = command.folder();
        final var filename = command.filename();
        final var video = this.gateway.findById(id).orElseThrow(() -> notFound(id));
        final var encodedPath = "%s/%s".formatted(folder, filename);

        if (matches(resourceId, video.getContent().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, command.status(), video, encodedPath);
        } else if (matches(resourceId, video.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, command.status(), video, encodedPath);
        }
    }

    private void updateVideo(final VideoMediaType type, final MediaStatus status, final Video video,
            final String encodedPath) {

        switch (status) {
            case PENDING -> {}
            case PROCESSING -> video.process(type);
            case COMPLETED -> video.complete(type, encodedPath);
        }

        this.gateway.update(video);
    }

    private boolean matches(final String id, final VideoMedia media) {
        if (media == null) {
            return false;
        }

        return media.getId().equals(id);
    }

    private NotFoundException notFound(final VideoID id) {
        return NotFoundException.with(Video.class, id);
    }

}