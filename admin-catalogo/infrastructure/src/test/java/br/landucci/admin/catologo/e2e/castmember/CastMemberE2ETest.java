package br.landucci.admin.catologo.e2e.castmember;

import br.landucci.admin.catologo.E2ETest;
import br.landucci.admin.catologo.domain.castmember.CastMemberID;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.e2e.MockDsl;
import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CastMemberRepository repository;

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
    public void asACatalogAdminIShouldBeAbleToNavigateInAllCastMembers() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var zelda = createCastMember("Zelda", CastMemberType.ACTOR);
        final var peach = createCastMember("Peach", CastMemberType.DIRECTOR);
        final var daisy = createCastMember("Daisy", CastMemberType.ACTOR);

        Assertions.assertEquals(3, repository.count());

        listCastMembers(0, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Daisy")));

        listCastMembers(1, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Peach")));

        listCastMembers(2, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Zelda")));

        listCastMembers(3, 1, "", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToFilterCastMembersByName() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var zelda = createCastMember("Zelda", CastMemberType.ACTOR);
        final var peach = createCastMember("Peach", CastMemberType.DIRECTOR);
        final var daisy = createCastMember("Daisy", CastMemberType.ACTOR);

        Assertions.assertEquals(3, repository.count());

        listCastMembers(0, 1, "eac", "name", "asc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Peach")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleTSortCastMembersByNameDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var zelda = createCastMember("Zelda", CastMemberType.ACTOR);
        final var peach = createCastMember("Peach", CastMemberType.DIRECTOR);
        final var daisy = createCastMember("Daisy", CastMemberType.ACTOR);

        Assertions.assertEquals(3, repository.count());

        listCastMembers(0, 3, "", "name", "desc")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo("Zelda")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[1].name", Matchers.equalTo("Peach")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[2].name", Matchers.equalTo("Daisy")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateAndRetrieveANewCastMember() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCastMember(expectedName, expectedType);
        final var output = retrieveCastMember(created.id());

        Assertions.assertEquals(created.id(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldReceiveANotFoundErrorWhenFindingACastMemberByAnInexistingID() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var request = MockMvcRequestBuilders.get("/castmember/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect((MockMvcResultMatchers.jsonPath("$.message",
                        Matchers.equalTo("CastMember with ID 123 was not found"))));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAnExistingCastMember() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;

        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCastMember("Peach", CastMemberType.DIRECTOR);
        final var expectedId = CastMemberID.with(created.id());

        final var updated = updateCastMember(expectedId, expectedName,expectedType);
        final var output = retrieveCastMember(updated.id());

        Assertions.assertEquals(expectedId.getValue(), output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedType, output.type());
        Assertions.assertNotNull(output.createdAt());
        Assertions.assertNotNull(output.updatedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAnExistingCastMember() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, repository.count());

        final var created = createCastMember("Zelda", CastMemberType.ACTOR);
        final var expectedId = CastMemberID.with(created.id());

        Assertions.assertEquals(1, repository.count());

        deleteCastMember(expectedId);

        Assertions.assertEquals(0, repository.count());
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }
}
