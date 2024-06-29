package br.landucci.admin.catologo.infrastructure.api;

import br.landucci.admin.catologo.ApiTest;
import br.landucci.admin.catologo.ControllerTest;
import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberOutputCommand;
import br.landucci.admin.catologo.application.castmember.create.DefaultCreateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.delete.DeleteCastMemberInputCommand;
import br.landucci.admin.catologo.application.castmember.find.DefaultFindCastMemberByIDUseCase;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDInputCommand;
import br.landucci.admin.catologo.application.castmember.find.FindCastMemberByIDOutputCommand;
import br.landucci.admin.catologo.application.castmember.list.DefaultListCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.list.ListCastMemberOutputCommand;
import br.landucci.admin.catologo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberOutputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMember;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.infrastructure.castmember.model.CreateCastMemberRequestCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.UpdateCastMemberRequestCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = CastMemberAPI.class)
class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultFindCastMemberByIDUseCase findCastMemberByIDUseCase;

    @MockBean
    private DefaultListCastMemberUseCase listCastMembersUseCase;

    @MockBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @Test
    void givenAValidCommand_whenCreatingACastMember_thenShouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedId = CastMemberID.with("o1i2u3i1o");
        final var expectedLocation = "/castmember/%s".formatted(expectedId.getValue());

        final var command = new CreateCastMemberRequestCommand(expectedName, expectedType);

        Mockito.when(this.createCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(CreateCastMemberOutputCommand.from(expectedId));

        final var request = MockMvcRequestBuilders.post("/castmember")
                .with(ApiTest.CASTMEMBERS_JWT)
                .with(ApiTest.CASTMEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", expectedLocation))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(this.createCastMemberUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    void givenAnInvalidNullName_whenCreatingACastMember_thenShouldReturnANotification() throws Exception {
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorMessage = "Name should not be null";

        final var command = new CreateCastMemberRequestCommand(null, expectedType);

        Mockito.when(this.createCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new ValidationError(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.post("/castmember")
                .with(ApiTest.CASTMEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(this.createCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(null, actualCmd.name()) &&
                Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    void givenAValidId_whenFindingById_thenShouldReturnIt() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;

        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId().getValue();

        Mockito.when(this.findCastMemberByIDUseCase.execute(Mockito.any()))
                .thenReturn(FindCastMemberByIDOutputCommand.from(castMember));

        final var request = MockMvcRequestBuilders.get("/castmember/{id}", expectedId)
                .with(ApiTest.CASTMEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(castMember.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(castMember.getUpdatedAt().toString())));

        final var input = FindCastMemberByIDInputCommand.with(expectedId);
        Mockito.verify(this.findCastMemberByIDUseCase).execute(input);
    }

    @Test
    void givenAnInvalidId_whenFindingACastMember_thenShouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var expectedId = CastMemberID.with("123");

        Mockito.when(this.findCastMemberByIDUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/castmember/{id}", expectedId.getValue())
                .with(ApiTest.CASTMEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        final var input = FindCastMemberByIDInputCommand.with(expectedId.getValue());
        Mockito.verify(this.findCastMemberByIDUseCase).execute(input);
    }

    @Test
    void givenAValidCommand_whenUpdatingACastMember_thenShouldReturnItsIdentifier() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var castMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = castMember.getId();

        final var command = new UpdateCastMemberRequestCommand(expectedName, expectedType);

        Mockito.when(this.updateCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(UpdateCastMemberOutputCommand.from(expectedId));

        final var request = MockMvcRequestBuilders.put("/castmember/{id}", expectedId.getValue())
                .with(ApiTest.CASTMEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id()) &&
                Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    void givenAnInvalidNullName_whenUpdatingCastMember_thenShouldReturnANotification() throws Exception {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.DIRECTOR);
        final var expectedId = castMember.getId();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "Name should not be null";

        final var command = new UpdateCastMemberRequestCommand(null, expectedType);

        Mockito.when(this.updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotificationException.with(new ValidationError(expectedErrorMessage)));

        final var request = MockMvcRequestBuilders.put("/castmember/{id}", expectedId.getValue())
                .with(ApiTest.CASTMEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id()) &&
                Objects.equals(null, cmd.name()) &&
                Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    void givenAnInvalidId_whenUpdatingCastMember_thenShouldReturnNotFound() throws Exception {
        final var expectedId = CastMemberID.with("123");
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.DIRECTOR;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";
        final var command = new UpdateCastMemberRequestCommand(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(CastMember.class, expectedId));

        final var request = MockMvcRequestBuilders.put("/castmember/{id}", expectedId.getValue())
                .with(ApiTest.CASTMEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(this.updateCastMemberUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId.getValue(), cmd.id()) &&
                Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedType, cmd.type())
        ));
    }

    @Test
    void givenAValidId_whenDeletingById_thenShouldDeleteIt() throws Exception {
        final var expectedId = "123";

        Mockito.doNothing().when(this.deleteCastMemberUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/castmember/{id}", expectedId)
        .with(ApiTest.CASTMEMBERS_JWT);
        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        final var input = DeleteCastMemberInputCommand.with(expectedId);
        Mockito.verify(deleteCastMemberUseCase).execute(input);
    }

    @Test
    void givenValidParams_whenListingCastMembers_thenShouldReturnIt() throws Exception {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);
        final var expectedPage = 1;
        final var expectedPerPage = 20;
        final var expectedTerms = "Alg";
        final var expectedSort = "type";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(ListCastMemberOutputCommand.from(castMember));

        Mockito.when(listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/castmember")
                .with(ApiTest.CASTMEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(castMember.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(castMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(castMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(castMember.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedTerms, query.terms())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedDirection, query.direction())
        ));
    }

    @Test
    void givenEmptyParams_whenListingCastMembers_thenShouldAndReturnAList() throws Exception {
        final var castMember = CastMember.newCastMember("Zelda", CastMemberType.ACTOR);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(ListCastMemberOutputCommand.from(castMember));

        Mockito.when(this.listCastMembersUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/castmember")
                .with(ApiTest.CASTMEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(castMember.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(castMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(castMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(castMember.getCreatedAt().toString())));

        Mockito.verify(listCastMembersUseCase).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page()) &&
                Objects.equals(expectedPerPage, query.perPage()) &&
                Objects.equals(expectedTerms, query.terms()) &&
                Objects.equals(expectedSort, query.sort()) &&
                Objects.equals(expectedDirection, query.direction())
        ));
    }
}
