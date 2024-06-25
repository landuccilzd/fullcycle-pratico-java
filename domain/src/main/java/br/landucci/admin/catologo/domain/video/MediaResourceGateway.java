package br.landucci.admin.catologo.domain.video;

import java.util.Optional;

public interface MediaResourceGateway {

    VideoMedia storeVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    Optional<Resource> getResource(VideoID anId, VideoMediaType type);

    void clearResources(VideoID anId);
}
