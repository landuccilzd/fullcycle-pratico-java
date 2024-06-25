package br.landucci.admin.catologo.infrastructure.castmember.presenter;

import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDOutputCommand;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberOutputCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.FindCastMemberByIDResponseCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.ListCastMembersResponseCommand;

public interface CastMemberAPIPresenter {

    static FindCastMemberByIDResponseCommand present(FindCastMemberByIDOutputCommand output) {
        return new FindCastMemberByIDResponseCommand(output.id(), output.name(), output.type(), output.createdAt(),
                output.updatedAt());
    }

    static ListCastMembersResponseCommand present(final ListCastMemberOutputCommand output) {
        return new ListCastMembersResponseCommand(output.id(), output.name(), output.type(), output.createdAt(),
                output.updatedAt()
        );
    }
}
