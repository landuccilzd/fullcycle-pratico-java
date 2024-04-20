package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.events.DomainEvent;
import br.landucci.admin.catologo.domain.utils.InstantUtils;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {

    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, InstantUtils.now());
    }
}
