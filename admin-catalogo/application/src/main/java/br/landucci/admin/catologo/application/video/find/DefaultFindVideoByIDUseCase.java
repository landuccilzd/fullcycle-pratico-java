package br.landucci.admin.catologo.application.video.find;

import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.video.Video;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoID;

import java.util.Objects;

public class DefaultFindVideoByIDUseCase extends FindVideoByIDUseCase {

    private final VideoGateway videoGateway;

    public DefaultFindVideoByIDUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public FindVideoByIDOutputCommand execute(final FindVideoByIDInputCommand input) {
        final var videoId = VideoID.with(input.id());
        return this.videoGateway.findById(videoId)
                .map(FindVideoByIDOutputCommand::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
    }
}
