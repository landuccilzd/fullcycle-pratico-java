package br.landucci.admin.catologo.infrastructure.genre;

import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreGateway;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreJpaEntity;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import br.landucci.admin.catologo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class GenreMySQLGateway implements GenreGateway {
    private final GenreRepository repository;

    public GenreMySQLGateway(GenreRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Optional<Genre> findById(final GenreID id) {
        return this.repository.findById(id.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery query) {
        final var page = PageRequest.of(query.page(), query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));

        final var specifications = Optional.ofNullable(query.terms()).filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<GenreJpaEntity>like("name", str)).orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).stream().toList()
        );
    }

    @Override
    public Genre create(final Genre genre) {
        return this.save(genre);
    }

    @Override
    public Genre update(final Genre genre) {
        return this.save(genre);
    }

    @Override
    public void deleteById(final GenreID id) {
        final var idValue = id.getValue();
        if (!this.repository.existsById(idValue)) {
            throw new IllegalArgumentException("Genre with ID %s was not found".formatted(id.getValue()));
        }
        this.repository.deleteById(idValue);
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> genreIDS) {
        final var ids = StreamSupport.stream(genreIDS.spliterator(), false).map(GenreID::getValue).toList();
        return this.repository.existsByIds(ids).stream().map(GenreID::from).toList();
    }

    private Genre save(final Genre genre) {
        final var entity = GenreJpaEntity.from(genre);
        return this.repository.save(entity).toAggregate();
    }
}