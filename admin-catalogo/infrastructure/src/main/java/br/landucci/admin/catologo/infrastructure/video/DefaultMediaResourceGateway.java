package br.landucci.admin.catologo.infrastructure.video;

import br.landucci.admin.catologo.domain.video.*;
import br.landucci.admin.catologo.infrastructure.configuration.properties.storage.StorageProperties;
import br.landucci.admin.catologo.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService service;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService service) {
        this.filenamePattern = Objects.requireNonNull(props.getFilenamePattern(), "Configure o Filename Pattern");
        this.locationPattern = Objects.requireNonNull(props.getLocationPattern(), "Configure o Location Pattern");
        this.service = Objects.requireNonNull(service);
    }

    @Override
    public VideoMedia storeVideo(final VideoID id, final VideoResource videoResource) {
        final var filepath = this.getFilepath(id, videoResource.type());
        final var resource = videoResource.resource();
        this.store(filepath, resource);
        return VideoMedia.with(resource.getChecksum(), resource.getName(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID id, final VideoResource videoResource) {
        final var filepath = this.getFilepath(id, videoResource.type());
        final var resource = videoResource.resource();
        this.store(filepath, resource);
        return ImageMedia.with(resource.getChecksum(), resource.getName(), filepath);
    }

    @Override
    public Optional<Resource> getResource(final VideoID id, final VideoMediaType type) {
        return this.service.get(this.getFilepath(id, type));
    }

    @Override
    public void clearResources(final VideoID id) {
        final var ids = this.service.list(getFolder(id));
        this.service.deleteAll(ids);
    }

    private String getFilename(final VideoMediaType type) {
        return filenamePattern.replace("{type}", type.name());
    }

    private String getFolder(final VideoID id) {
        return locationPattern.replace("{videoId}", id.getValue());
    }

    private String getFilepath(final VideoID id, final VideoMediaType type) {
        return getFolder(id).concat("/").concat(getFilename(type));
    }

    private void store(final String filepath, final Resource resource) {
        this.service.store(filepath, resource);
    }
}
