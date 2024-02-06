package br.landucci.admin.catologo.infrastructure.api;

import br.landucci.admin.catologo.ControllerTest;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryUseCase;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryInputCommand;
import br.landucci.admin.catologo.application.category.delete.DeleteCategoryUseCase;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryInputCommand;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.find.FindByIDCategoryUseCase;
import br.landucci.admin.catologo.application.category.list.ListCategoriesOutputCommand;
import br.landucci.admin.catologo.application.category.list.ListCategoriesUseCase;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryUseCase;
import br.landucci.admin.catologo.domain.category.Category;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.pagination.SearchQuery;
import br.landucci.admin.catologo.domain.exception.NotFoundException;
import br.landucci.admin.catologo.domain.pagination.Pagination;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.handler.Notification;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.models.UpdateCategoryRequestCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
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

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;
    @MockBean
    private FindByIDCategoryUseCase findByIDCategoryUseCase;
    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    public void givenValidParams_whenListingCategories_shouldReturnAListOfCategories() throws Exception {
        // GIVEN
        final var category = Category.newCategory("Ficção Científica", null, true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ficção";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(ListCategoriesOutputCommand.from(category));

        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                expectedDirection);

        Mockito.when(listCategoriesUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // WHEN
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(category.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(category.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(category.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(category.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(category.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(category.getDeletedAt())));

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(qry ->
                Objects.equals(expectedPage, qry.page())
                        && Objects.equals(expectedPerPage, qry.perPage())
                        && Objects.equals(expectedDirection, qry.direction())
                        && Objects.equals(expectedSort, qry.sort())
                        && Objects.equals(expectedTerms, qry.terms())
        ));
    }

    @Test
    public void givenAValidInput_whenFindingACategory_thenShouldReturnTheCategory() throws Exception {
        // GIVEN
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();
        final var input = FindByIDCategoryInputCommand.with(expectedId.getValue());

        Mockito.when(findByIDCategoryUseCase.execute(Mockito.any()))
                .thenReturn(FindByIDCategoryOutputCommand.from(category));

        // WHEN
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(category.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(category.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(category.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(category.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(category.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(category.getDeletedAt())))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(findByIDCategoryUseCase, Mockito.times(1)).execute(Mockito.eq(input));
    }

    @Test
    public void givenAnInvalidID_whenFindingACategory_thenShouldReturnNotFound() throws Exception {
        // GIVEN
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(findByIDCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // WHEN
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCreateACategory_thenShouldReturnACreatedCategory() throws Exception {
        // GIVEN - DADO
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var input =
                new CreateCategoryRequestCommand(expectedName, expectedDescription, expectedActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutputCommand.from("123")));

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123"))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123")));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(command ->
                Objects.equals(expectedName, command.name())
                && Objects.equals(expectedDescription, command.description())
                && Objects.equals(expectedActive, command.active())
        ));
    }

    @Test
    public void givenAValidCommand_whenUpdatingACategory_thenShouldReturnAnUpdatedCategory() throws Exception {
        // GIVEN - DADO
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var input = new UpdateCategoryRequestCommand(expectedName, expectedDescription, expectedActive);

        final var category = Category.newCategory(expectedName, expectedDescription, expectedActive);
        final var expectedId = category.getId();

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(UpdateCategoryOutputCommand.from(category)));

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.getValue())))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(command ->
                Objects.equals(expectedId.getValue(), command.id())
                        && Objects.equals(expectedName, command.name())
                        && Objects.equals(expectedDescription, command.description())
                        && Objects.equals(expectedActive, command.active())
        ));
    }

    @Test
    public void givenAInvalidId_whenUpdatingACategory_thenShouldReturnAnErrorMessage() throws Exception {
        // GIVEN - DADO
        final var expectedId = "not-found";
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var input = new UpdateCategoryRequestCommand(expectedName, expectedDescription, expectedActive);
        final var expectedErrorMessage = "Category with ID not-found was not found";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAInvalidNullNameCommand_whenUpdatingACategory_thenShouldReturnAnErrorMessage() throws Exception {
        // GIVEN - DADO
        final var expectedId = "123";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;
        final var input = new UpdateCategoryRequestCommand(null, expectedDescription, expectedActive);
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new ValidationError(expectedErrorMessage))));

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(input));

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenDeletingACategory_thenShouldReturnNoContent() throws Exception {
        // GIVEN - DADO
        final var expectedId = "123";
        final var input = DeleteCategoryInputCommand.with(expectedId);

        Mockito.doNothing().when(deleteCategoryUseCase).execute(Mockito.any());

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1)).execute(Mockito.eq(input));
    }

    @Test
    public void givenAnInvalidId_whenDeletingACategory_thenShouldReturnAnErrorMessage() throws Exception {
        // GIVEN - DADO
        final var expectedId = "not-found";
        final var input = DeleteCategoryInputCommand.with(expectedId);
        final var expectedErrorMessage = "Category with ID not-found was not found";

        Mockito.doThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)))
                .when(deleteCategoryUseCase).execute(Mockito.any());

        // WHEN - QUANDO
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // THEN - ENTÃO
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));
    }

}