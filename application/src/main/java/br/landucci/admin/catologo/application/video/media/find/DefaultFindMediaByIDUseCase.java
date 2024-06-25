package br.landucci.admin.catologo.application.video.media.find;

import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.video.MediaResourceGateway;
import br.landucci.admin.catologo.domain.video.VideoID;
import br.landucci.admin.catologo.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultFindMediaByIDUseCase extends FindMediaByIDUseCase {

    private final MediaResourceGateway gateway;

    public DefaultFindMediaByIDUseCase(final MediaResourceGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public FindMediaByIDOutputCommand execute(final FindMediaByIDInputCommand command) {
        final var id = VideoID.with(command.videoId());
        final var type = VideoMediaType.of(command.mediaType()).orElseThrow(() ->
                typeNotFound(command.mediaType()));

        final var resource = this.gateway.getResource(id, type).orElseThrow(() ->
                notFound(command.videoId(), command.mediaType()));

        return FindMediaByIDOutputCommand.with(resource);
    }

    private NotFoundException notFound(final String id, final String type) {
        return NotFoundException.with(new ValidationError("Resource %s not found for video %s".formatted(type, id)));
    }

    private NotFoundException typeNotFound(final String type) {
        return NotFoundException.with(new ValidationError("Media type %s doesn't exists".formatted(type)));
    }

}