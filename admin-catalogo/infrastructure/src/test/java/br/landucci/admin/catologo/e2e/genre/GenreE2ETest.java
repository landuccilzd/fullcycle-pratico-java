package br.landucci.admin.catologo.e2e.genre;

import br.landucci.admin.catologo.E2ETest;
import br.landucci.admin.catologo.domain.category.CategoryID;
import br.landucci.admin.catologo.domain.genre.Genre;
import br.landucci.admin.catologo.domain.genre.GenreID;
import br.landucci.admin.catologo.e2e.MockDsl;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreJpaEntity;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@E2ETest
@Testcontainers
class GenreE2ETest implements MockDsl {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreRepository repository;
    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.34")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos_e2e");

    @DynamicPropertySource
    static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var port = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s \n", port);
        registry.add("E2E_MYSQL_PORT", () -> port);
        registry.add("DATABASE_MYSQL_USER", () -> "root");
        registry.add("DATABASE_MYSQL_PASS", () -> "123456");
    }

    @Test
    void asACatalogAdminIShouldBeAbleToNavigateInAllGenres() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.repository.count());

        final var terror = createGenre("Terror",true, List.<CategoryID>of());
        final var suspense = createGenre("Suspense",true, List.<CategoryID>of());
        final var drama = createGenre("Drama", true, List.<CategoryID>of());

        Assertions.assertEquals(3, this.repository.count());

        listGenres(0, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(1, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Suspense")));

        listGenres(2, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Terror")));

        listGenres(3, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToFilterGenresByName() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.repository.count());

        final var terror = createGenre("Terror",true, List.<CategoryID>of());
        final var suspense = createGenre("Suspense",true, List.<CategoryID>of());
        final var drama = createGenre("Drama", true, List.<CategoryID>of());

        Assertions.assertEquals(3, repository.count());

        listGenres(0, 1, "Ter", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Terror")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleTSortTerrorByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var terror = createGenre("Terror",true, List.<CategoryID>of());
        final var suspense = createGenre("Suspense",true, List.<CategoryID>of());
        final var drama = createGenre("Drama", true, List.<CategoryID>of());

        Assertions.assertEquals(3, repository.count());

        listGenres(0, 3, "", "name", "desc")
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
    void asACatalogAdminIShouldBeAbleToRetrieveACategory() throws Exception {
        final var filmes = createCategory("Filmes", null, true);
        final var expectedName = "Ficcao Cientifica";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from(filmes.id()));

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var createdGenre = createGenre(expectedName, expectedActive, expectedCategories);
        final var output = retrieveGenre(createdGenre.id());

        Assertions.assertEquals(createdGenre.id(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedActive, output.active());
        Assertions.assertEquals(expectedCategories.size(), output.categoriesCount());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
        Assertions.assertNull(output.deletedAt());
    }

    /*
     * EU COMO administrador de gêneros
     * PRECISO criar novos gêneros com valores validos
     * PARA que seja possível classificar as categorias
     */
    @Test
    void asAGenreAdmin_iNeedToCreateANewGenreWithCategories_toClassifyCategories() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.repository.count());

        final var expectedName = "Drama";
        final var expectedActive = true;
        final var filmes = createCategory("Filmes", null, true);
        final var series = createCategory("Séries", null, true);
        final var documentarios = createCategory("Documentarios", null, true);

        final var expectedCategories = List.of(
                CategoryID.from(filmes.id()),
                CategoryID.from(series.id()),
                CategoryID.from(documentarios.id())
        );

        final var createdGenre = createGenre(expectedName, expectedActive, expectedCategories);
        final var output = this.repository.findById(createdGenre.id()).get();

        Assertions.assertEquals(createdGenre.id(), output.getId());
        Assertions.assertEquals(expectedName, output.getName());
        Assertions.assertEquals(expectedActive, output.isActive());
        Assertions.assertEquals(expectedCategories.size(), output.categoriesCount());
        Assertions.assertNotNull(output.getCreatedAt());
        Assertions.assertNotNull(output.getUpdatedAt());
        Assertions.assertNull(output.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateAnExistingGenre() throws Exception {
        final var filmes = createCategory("Filmes", null, true);
        final var expectedName = "Suspense";
        final var expectedActive = true;
        final var expectedCategories = List.of(CategoryID.from(filmes.id()));

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var genre = Genre.newGenre("Drama", false);
        genre.adicionarCategorias(List.of(CategoryID.from("123")));
        this.repository.saveAndFlush(GenreJpaEntity.from(genre));

        final var updatedGenre = updateGenre(genre.getId(), expectedName, expectedActive, expectedCategories);
        final var output = this.repository.findById(genre.getId().getValue()).get();

        Assertions.assertEquals(updatedGenre.id(), output.getId());
        Assertions.assertEquals(expectedName, output.getName());
        Assertions.assertEquals(expectedActive, output.isActive());
        Assertions.assertEquals(expectedCategories.size(), output.categoriesCount());
        Assertions.assertNotNull(output.getCreatedAt());
        Assertions.assertNotNull(output.getUpdatedAt());
        Assertions.assertNull(output.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteAnExistingGenre() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createGenre("Terror", true, List.<CategoryID>of());
        final var expectedId = GenreID.from(created.id());

        Assertions.assertEquals(1, repository.count());

        deleteGenre(expectedId);

        Assertions.assertEquals(0, repository.count());
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }
}
