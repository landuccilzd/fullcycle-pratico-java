package br.landucci.admin.catologo.domain.castmember;

import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    Optional<CastMember> findById(CastMemberID id);
    Pagination<CastMember> findAll(SearchQuery query);
    CastMember create(CastMember castMember);
    CastMember update(CastMember castMember);
    void deleteById(CastMemberID id);
    List<CastMemberID> existsByIds(final Iterable<CastMemberID> castMemberIDS);
}
