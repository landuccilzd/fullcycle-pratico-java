package br.landucci.admin.catologo.infrastructure.api.controllers;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDInputCommand;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDUseCase;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberUseCase;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.infrastructure.api.CastMemberAPI;
import br.landucci.admin.catologo.infrastructure.castmember.model.CreateCastMemberRequestCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.FindCastMemberByIDResponseCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.ListCastMembersResponseCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.UpdateCastMemberRequestCommand;
import br.landucci.admin.catologo.infrastructure.castmember.presenter.CastMemberAPIPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
    private final ListCastMemberUseCase listCastMemberUseCase;
    private final FindCastMemberByIDUseCase findByIDCastMemberUseCase;
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    public CastMemberController(final ListCastMemberUseCase listCastMemberUseCase,
                                final FindCastMemberByIDUseCase findByIDCastMemberUseCase,
                                final CreateCastMemberUseCase createCastMemberUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase) {
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
        this.findByIDCastMemberUseCase = Objects.requireNonNull(findByIDCastMemberUseCase);
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    }

    @Override
    public Pagination<ListCastMembersResponseCommand> list(String search, int page, int perPage, String sort, String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return listCastMemberUseCase.execute(query).map(CastMemberAPIPresenter::present);
    }

    @Override
    public FindCastMemberByIDResponseCommand find(final String id) {
        final var input = FindCastMemberByIDInputCommand.with(id);
        final var output = this.findByIDCastMemberUseCase.execute(input);
        return CastMemberAPIPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequestCommand command) {
        final var input = CreateCastMemberInputCommand.with(command.name(), command.type());
        final var output = this.createCastMemberUseCase.execute(input);
        return ResponseEntity.created(URI.create("/castmember/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateCastMemberRequestCommand command) {
        final var input = UpdateCastMemberInputCommand.with(id, command.name(), command.type());
        final var output = this.updateCastMemberUseCase.execute(input);
        return ResponseEntity.ok().body(output);
    }

    @Override
    public void delete(String id) {
        final var input = DeleteCastMemberInputCommand.with(id);
        this.deleteCastMemberUseCase.execute(input);
    }

}