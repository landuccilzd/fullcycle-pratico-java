package br.landucci.admin.catologo.application.castmember.list;

import br.landucci.admin.catologo.domain.castmember.CastMemberGateway;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;

import java.util.Objects;

public final class DefaultListCastMemberUseCase extends ListCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultListCastMemberUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<ListCastMemberOutputCommand> execute(final SearchQuery aQuery) {
        return this.gateway.findAll(aQuery).map(ListCastMemberOutputCommand::from);
    }

}