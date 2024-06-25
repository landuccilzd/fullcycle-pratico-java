package br.landucci.admin.catologo.application.video.media.upload;

import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {

    private final MediaResourceGateway gateway;
    private final VideoGateway videoGateway;

    public DefaultUploadMediaUseCase(
            final MediaResourceGateway gateway,
            final VideoGateway videoGateway
    ) {
        this.gateway = Objects.requireNonNull(gateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public UploadMediaOutputCommand execute(final UploadMediaInputCommand command) {
        final var id = VideoID.with(command.videoId());
        final var resource = command.videoResource();

        final var video = this.videoGateway.findById(id).orElseThrow(() -> notFound(id));

        switch (resource.type()) {
            case VIDEO -> video.updateContent(gateway.storeVideo(id, resource));
            case TRAILER -> video.updateTrailer(gateway.storeVideo(id, resource));
            case BANNER -> video.updateBanner(gateway.storeImage(id, resource));
            case THUMBNAIL -> video.updateThumbnail(gateway.storeImage(id, resource));
            case THUMBNAIL_HALF -> video.updateThumbnailHalf(gateway.storeImage(id, resource));
        }

        return UploadMediaOutputCommand.with(videoGateway.update(video), resource.type());
    }

    private NotFoundException notFound(final VideoID id) {
        return NotFoundException.with(Video.class, id);
    }
}
