package br.landucci.admin.catologo.infrastructure.castmember;

import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
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
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID id) {
        return this.repository.findById(id.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember create(final CastMember castMember) {
        return save(castMember);
    }

    @Override
    public CastMember update(final CastMember castMember) {
        return save(castMember);
    }

    @Override
    public void deleteById(final CastMemberID id) {
        final var anId = id.getValue();
        if (this.repository.existsById(anId)) {
            this.repository.deleteById(anId);
        }
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> castMemberIDS) {
        final var ids = StreamSupport.stream(castMemberIDS.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();

        return this.repository.existsByIds(ids).stream()
                .map(CastMemberID::with)
                .toList();
    }

    private CastMember save(final CastMember castMember) {
        return this.repository.save(CastMemberJpaEntity.from(castMember)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
