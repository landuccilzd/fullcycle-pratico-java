package br.landucci.admin.catologo.infrastructure.video;

import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.utils.CollectionUtils;
import br.landucci.admin.catologo.domain.Identifier;
import br.landucci.admin.catologo.domain.video.*;
import br.landucci.admin.catologo.infrastructure.utils.SqlUtils;
import br.landucci.admin.catologo.infrastructure.video.persistence.VideoJpaEntity;
import br.landucci.admin.catologo.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class DefaultVideoGateway implements VideoGateway {

//    private final EventService eventService;
    private final VideoRepository repository;

    public DefaultVideoGateway(
//            @VideoCreatedQueue final EventService eventService,
            final VideoRepository videoRepository) {

//        this.eventService = Objects.requireNonNull(eventService);
        this.repository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID id) {
        return this.repository.findById(id.getValue()).map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var actualPage = this.repository.findAll(
                SqlUtils.like(SqlUtils.upper(query.terms())),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(query.castMembers(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(query.categories(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(query.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(actualPage.getNumber(), actualPage.getSize(), actualPage.getTotalElements(),
                actualPage.toList());
    }

    @Override
    @Transactional
    public Video create(final Video video) {
        return save(video);
    }

    @Override
    @Transactional
    public Video update(final Video video) {
        return save(video);
    }

    @Override
    public void deleteById(final VideoID id) {
        final var aVideoId = id.getValue();

        if (this.repository.existsById(aVideoId)) {
            this.repository.deleteById(aVideoId);
        }
    }

    private Video save(final Video aVideo) {
        final var result = this.repository.save(VideoJpaEntity.from(aVideo)).toAggregate();
//        aVideo.publishDomainEvents(this.eventService::send);
        return result;
    }
}