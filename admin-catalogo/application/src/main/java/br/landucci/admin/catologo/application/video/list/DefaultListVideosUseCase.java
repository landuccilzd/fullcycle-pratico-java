package br.landucci.admin.catologo.application.video.list;

import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.video.VideoGateway;
import br.landucci.admin.catologo.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase {

    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<ListVideoOutputCommand> execute(final VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery).map(ListVideoOutputCommand::from);
    }
}
