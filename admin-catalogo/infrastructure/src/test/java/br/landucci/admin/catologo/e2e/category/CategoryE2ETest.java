package br.landucci.admin.catologo.e2e.category;

import br.landucci.admin.catologo.E2ETest;
import br.landucci.admin.catologo.application.category.create.CreateCategoryOutputCommand;
import br.landucci.admin.catologo.application.category.update.UpdateCategoryOutputCommand;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.infrastructure.category.models.CreateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.models.FindByIdCategoryResponseCommand;
import br.landucci.admin.catologo.infrastructure.category.models.UpdateCategoryRequestCommand;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.hamcrest.Matchers;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository repository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.34")
                    .withPassword("123456").withUsername("root").withDatabaseName("adm_videos_e2e");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var port = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s \n", port);
        registry.add("E2E_MYSQL_PORT", () -> port);
        registry.add("DATABASE_MYSQL_USER", () -> "root");
        registry.add("DATABASE_MYSQL_PASS", () -> "123456");
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateInAllCategories() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var terror = createCategory("Terror", "Filmes de terror", true);
        final var suspense = createCategory("Suspense", "Filmes de suspense", true);
        final var drama = createCategory("Drama", "Filmes de drama", true);

        Assertions.assertEquals(3, repository.count());

        listCategories(0, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listCategories(1, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Suspense")));

        listCategories(2, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Terror")));

        listCategories(3, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToFilterCategoriesByName() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var terror = createCategory("Terror", "Filmes de terror", true);
        final var suspense = createCategory("Suspense", "Filmes de suspense", true);
        final var drama = createCategory("Drama", "Filmes de drama", true);

        Assertions.assertEquals(3, repository.count());

        listCategories(0, 1, "Ter", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Terror")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleTSortCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var terror = createCategory("Terror", "Filmes de terror", true);
        final var suspense = createCategory("Suspense", "Filmes de suspense", true);
        final var drama = createCategory("Drama", "Filmes de drama", true);

        Assertions.assertEquals(3, repository.count());

        listCategories(0, 3, "", "description", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Terror")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Suspense")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Drama")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateAndRetrieveANewCategory() throws Exception {
        final var expectedName = "Ficcao Cientifica";
        final var expectedDescription = "Filmes de ficcao cientifica";
        final var expectedActive = true;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCategory(expectedName, expectedDescription, expectedActive);
        final var output = retrieveCategory(created.id());

        Assertions.assertEquals(created.id(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldReceiveANotFoundErrorWhenFindingACategoryByAnInexistingID() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var request = MockMvcRequestBuilders.get("/categories/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect((MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("Category with ID 123 was not found"))));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAnExistingCategory() throws Exception {
        final var expectedName = "Suspense";
        final var expectedDescription = "Filmes de suspense";
        final var expectedActive = true;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCategory("Terror", "Filmes de terror", true);
        final var expectedId = CategoryID.from(created.id());

        final var updated = updateCategory(expectedId, expectedName,expectedDescription, expectedActive);
        final var output = retrieveCategory(updated.id());

        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAnExistingCategory() throws Exception {
        final var expectedName = "Suspense";
        final var expectedDescription = "Filmes de suspense";
        final var expectedActive = true;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCategory(expectedName, expectedDescription, false);
        final var expectedId = CategoryID.from(created.id());

        final var updated = updateCategory(expectedId, expectedName,expectedDescription, expectedActive);
        final var output = retrieveCategory(updated.id());

        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInctivateAnExistingCategory() throws Exception {
        final var expectedName = "Suspense";
        final var expectedDescription = "Filmes de suspense";
        final var expectedActive = false;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCategory(expectedName, expectedDescription, true);
        final var expectedId = CategoryID.from(created.id());

        final var updated = updateCategory(expectedId, expectedName,expectedDescription, expectedActive);
        final var output = retrieveCategory(updated.id());

        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDescription, output.description());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNotNull(output.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAnExistingCategory() throws Exception {
        final var expectedName = "Suspense";
        final var expectedDescription = "Filmes de suspense";
        final var expectedActive = true;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCategory("Terror", "Filmes de terror", true);
        final var expectedId = CategoryID.from(created.id());

        Assertions.assertEquals(1, repository.count());

        deleteCategory(expectedId);

        Assertions.assertEquals(0, repository.count());
    }

    private ResultActions listCategories(final int page, final int perPage, final String search,
                final String sort, final String direction) throws Exception {
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(request);
    }

    private FindByIdCategoryResponseCommand retrieveCategory(final String id) throws Exception {
        final var uri = "/categories/%s".formatted(id);
        final var request = MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, FindByIdCategoryResponseCommand.class);
    }

    private CreateCategoryOutputCommand createCategory(final String name, final String description, final boolean active) throws Exception {
        final var body = new CreateCategoryRequestCommand(name, description, active);

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, CreateCategoryOutputCommand.class);
    }

    private UpdateCategoryOutputCommand updateCategory(final CategoryID id, final String name, final String description,
                final boolean active) throws Exception {
        final var body = new UpdateCategoryRequestCommand(name, description, active);

        final var uri = "/categories/%s".formatted(id.getValue());
        final var request = MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return Json.readValue(json, UpdateCategoryOutputCommand.class);
    }

    private void deleteCategory(final CategoryID id) throws Exception {

        final var uri = "/categories/%s".formatted(id.getValue());
        final var request = MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
