package br.landucci.admin.catologo.application.video.media.find;

import br.landucci.admin.catologo.domain.video.Resource;

public record FindMediaByIDOutputCommand(byte[] content, String contentType, String name) {

    public static FindMediaByIDOutputCommand with(final Resource resource) {
        return new FindMediaByIDOutputCommand(resource.getContent(), resource.getContentType(), resource.getName());
    }
}
