package br.landucci.admin.catologo.infrastructure.api;

import br.landucci.admin.catologo.ApiTest;
import br.landucci.admin.catologo.ControllerTest;
import br.landucci.admin.catologo.application.genre.create.CreateGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.create.CreateGenreUseCase;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreInputCommand;
import br.landucci.admin.catologo.application.genre.delete.DeleteGenreUseCase;
import br.landucci.admin.catologo.application.genre.find.FindByIDGenreInputCommand;
import br.landucci.admin.catologo.application.genre.find.FindByIdGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.find.FindGenreByIDUseCase;
import br.landucci.admin.catologo.application.genre.list.ListGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.list.ListGenreUseCase;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreUseCase;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import br.landucci.admin.catologo.infrastructure.genre.models.CreateGenreRequestCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.UpdateGenreRequestCommand;
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

@ControllerTest(controllers = GenreAPI.class)
class GenreAPITest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ListGenreUseCase listGenreUseCase;
    @MockBean
    private FindGenreByIDUseCase findGenreByIDUseCase;
    @MockBean
    private CreateGenreUseCase createGenreUseCase;
    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;
    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @Test
    void givenValidParams_whenListingGenres_shouldReturnGenres() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var genre = Genre.newGenre("Ação", false);
        final var expectedItems = List.of(ListGenreOutputCommand.from(genre));

        Mockito.when(this.listGenreUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/genres")
                .with(ApiTest.GENRES_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(genre.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(genre.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(genre.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())));

        Mockito.verify(this.listGenreUseCase).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
        ));
    }

    @Test
    void givenAValidId_whenFindingAGenreById_shouldReturnGenre() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        genre.adicionarCategorias(expectedCategories.stream().map(CategoryID::from).toList());

        final var expectedId = genre.getId().getValue();

        Mockito.when(this.findGenreByIDUseCase.execute(Mockito.any()))
                .thenReturn(FindByIdGenreOutputCommand.from(genre));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(genre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(genre.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(genre.getDeletedAt().toString())));

        final var input = FindByIDGenreInputCommand.with(expectedId);
        Mockito.verify(findGenreByIDUseCase).execute(input);
    }

    @Test
    void givenAnInvalidId_whenFindingGenreById_shouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;

        Mockito.when(this.findGenreByIDUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        final var request = MockMvcRequestBuilders.get("/genres/{id}", expectedId.getValue())
                .with(ApiTest.GENRES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        final var input = FindByIDGenreInputCommand.with(expectedId.getValue());
        Mockito.verify(this.findGenreByIDUseCase).execute(input);
    }

    @Test
    void givenAValidCommand_whenCreatingAGenre_thenShouldReturnACreatedGenre() throws Exception {
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of("456", "789");
        final var expectedId = "123";
        final var expectedLocation = "/genres/%s".formatted(expectedId);
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;
        final var input = new CreateGenreRequestCommand(expectedName, expectedActive, expectedCategories);

        Mockito.when(this.createGenreUseCase.execute(Mockito.any()))
                .thenReturn(CreateGenreOutputCommand.with(expectedId));

        final var request = MockMvcRequestBuilders.post("/genres")
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", expectedLocation))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(this.createGenreUseCase, Mockito.times(1)).execute(Mockito.argThat(command ->
                Objects.equals(expectedName, command.name())
                && Objects.equals(expectedActive, command.active())
                && Objects.equals(expectedCategories, command.categories())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;

        final var command = new CreateGenreRequestCommand(null, expectedIsActive, expectedCategories);

        Mockito.when(this.createGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.post("/genres")
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(this.createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(null, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    void givenAValidCommand_whenUpdatingGenre_shouldReturnGenreId() throws Exception {
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;

        final var genre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = genre.getId().getValue();

        final var command = new UpdateGenreRequestCommand(expectedName, expectedIsActive,expectedCategories);

        Mockito.when(updateGenreUseCase.execute(Mockito.any())).thenReturn(UpdateGenreOutputCommand.from(genre));

        final var aRequest = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(aRequest);

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(this.updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    void givenAnInvalidName_whenUpdatingGenre_shouldReturnNotification() throws Exception {
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Name should not be null";
        final var expectedMediaType = MediaType.APPLICATION_JSON_VALUE;

        final var genre = Genre.newGenre("Ação", expectedIsActive);
        final var expectedId = genre.getId().getValue();

        final var command = new UpdateGenreRequestCommand(null, expectedIsActive, expectedCategories);

        Mockito.when(updateGenreUseCase.execute(Mockito.any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var request = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", expectedMediaType))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(null, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    void givenAValidId_whenDeletingGenre_shouldBeOK() throws Exception {
        final var expectedId = "123";
        final var input = DeleteGenreInputCommand.with(expectedId);

        Mockito.doNothing().when(this.deleteGenreUseCase).execute(Mockito.any());

        final var request = MockMvcRequestBuilders.delete("/genres/{id}", input.id())
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(request);

        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(this.deleteGenreUseCase).execute(input);
    }

}