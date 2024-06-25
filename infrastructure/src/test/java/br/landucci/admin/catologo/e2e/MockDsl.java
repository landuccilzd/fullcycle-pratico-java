package br.landucci.admin.catologo.e2e;

import br.landucci.admin.catologo.application.castmember.create.CreateCastMemberOutputCommand;
import br.landucci.admin.catologo.application.castmember.update.UpdateCastMemberOutputCommand;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryOutputCommand;
import br.landucci.admin.catologo.application.genre.create.CreateGenreOutputCommand;
import br.landucci.admin.catologo.application.genre.update.UpdateGenreOutputCommand;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.infrastructure.castmember.model.CreateCastMemberRequestCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.FindCastMemberByIDResponseCommand;
import br.landucci.admin.catologo.infrastructure.castmember.model.UpdateCastMemberRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.models.FindByIdCategoryResponseCommand;
import br.landucci.admin.catologo.infrastructure.category.models.UpdateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.genre.models.CreateGenreRequestCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.FindGenreByIdResponseCommand;
import br.landucci.admin.catologo.infrastructure.genre.models.UpdateGenreRequestCommand;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.function.Function;

public interface MockDsl {
    MockMvc mvc();

    default ResultActions listCategories(final int page, final int perPage, final String search,
            final String sort, final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    default FindByIdCategoryResponseCommand retrieveCategory(final String id) throws Exception {
        final var url = "/categories/%s".formatted(id);
        final var json = this.retrieve(url);
        return Json.readValue(json, FindByIdCategoryResponseCommand.class);
    }

    default CreateCategoryOutputCommand createCategory(final String name, final String description,
            final boolean active) throws Exception {
        final var body = new CreateCategoryRequestCommand(name, description, active);
        final var url = "/categories";
        final var json = this.create(url, body);
        return Json.readValue(json, CreateCategoryOutputCommand.class);
    }

    default UpdateCategoryOutputCommand updateCategory(final CategoryID id, final String name,
            final String description, final boolean active) throws Exception {
        final var body = new UpdateCategoryRequestCommand(name, description, active);
        final var url = "/categories/%s".formatted(id.getValue());
        final var json = this.update(url, body);
        return Json.readValue(json, UpdateCategoryOutputCommand.class);
    }

    default void deleteCategory(final CategoryID id) throws Exception {
        final var url = "/categories/%s".formatted(id.getValue());
        this.delete(url);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort,
            final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }

    default FindGenreByIdResponseCommand retrieveGenre(final String id) throws Exception {
        final var url = "/genres/%s".formatted(id);
        final var json = this.retrieve(url);
        return Json.readValue(json, FindGenreByIdResponseCommand.class);
    }

    default CreateGenreOutputCommand createGenre(final String name, final boolean active,
            final List<CategoryID> categories) throws Exception {
        final var body = new CreateGenreRequestCommand(name, active, mapTo(categories, CategoryID::getValue));
        final var url = "/genres";
        final var json = this.create(url, body);
        return Json.readValue(json, CreateGenreOutputCommand.class);
    }

    default UpdateGenreOutputCommand updateGenre(final GenreID id, final String name, final boolean active,
            final List<CategoryID> categories) throws Exception {
        final var body = new UpdateGenreRequestCommand(name, active, mapTo(categories, CategoryID::getValue));
        final var url = "/genres/%s".formatted(id.getValue());
        final var json = this.update(url, body);
        return Json.readValue(json, UpdateGenreOutputCommand.class);
    }

    default void deleteGenre(final GenreID id) throws Exception {
        final var url = "/genres/%s".formatted(id.getValue());
        this.delete(url);
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search,
                                         final String sort, final String direction) throws Exception {
        return this.list("/castmember", page, perPage, search, sort, direction);
    }

    default FindCastMemberByIDResponseCommand retrieveCastMember(final String id) throws Exception {
        final var url = "/castmember/%s".formatted(id);
        final var json = this.retrieve(url);
        return Json.readValue(json, FindCastMemberByIDResponseCommand.class);
    }

    default CreateCastMemberOutputCommand createCastMember(final String name, final CastMemberType type)
            throws Exception {
        final var body = new CreateCastMemberRequestCommand(name, type);
        final var url = "/castmember";
        final var json = this.create(url, body);
        return Json.readValue(json, CreateCastMemberOutputCommand.class);
    }

    default UpdateCastMemberOutputCommand updateCastMember(final CastMemberID id, final String name,
            final CastMemberType type) throws Exception {
        final var body = new UpdateCastMemberRequestCommand(name, type);
        final var url = "/castmember/%s".formatted(id.getValue());
        final var json = this.update(url, body);
        return Json.readValue(json, UpdateCastMemberOutputCommand.class);
    }

    default void deleteCastMember(final CastMemberID id) throws Exception {
        final var url = "/castmember/%s".formatted(id.getValue());
        this.delete(url);
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search,
                                         final String sort, final String direction) throws Exception {
        final var request = MockMvcRequestBuilders.get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(request);
    }

    private String retrieve(final String url) throws Exception {
        final var request = MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String create(final String url, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();
    }

    private String update(final String url, final Object body) throws Exception {
        final var request = MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    private void delete(final String url) throws Exception {
        final var request = MockMvcRequestBuilders.delete(url).contentType(MediaType.APPLICATION_JSON);
        this.mvc().perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    default <S, D> List<D> mapTo(final List<S> source, final Function<S, D> mapper) {
        return source.stream().map(mapper).toList();
    }


}
