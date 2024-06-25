package br.landucci.admin.catologo.application.video.list;

import br.landucci.admin.catologo.application.UseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<ListVideoOutputCommand>> {
}
