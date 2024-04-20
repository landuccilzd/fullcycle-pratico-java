package br.landucci.admin.catologo.infrastructure.category.models;

import br.landucci.admin.catologo.JacksonTest;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class ListCategoriesResponseCommandTest {

    @Autowired
    private JacksonTester<ListCategoriesResponseCommand> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Ficção Científica";
        final var expectedDescrition = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var responseCommand = new ListCategoriesResponseCommand(expectedId, expectedName, expectedDescrition,
                expectedActive, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        final var responseJson = this.json.write(responseCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescrition)
                .hasJsonPathValue("$.is_active", expectedActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Ficção Científica";
        final var expectedDescrition = "Filmes de ficção científica";
        final var expectedActive = true;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var strJson = """
            {
                "id":"%s",
                "name":"%s",
                "description":"%s",
                "is_active":"%s",
                "created_at":"%s",
                "updated_at":"%s",
                "deleted_at":"%s"
            }
        """.formatted(expectedId, expectedName, expectedDescrition, expectedActive, expectedCreatedAt,
                expectedUpdatedAt, expectedDeletedAt);

        final var responseCommand = this.json.parse(strJson);

        Assertions.assertThat(responseCommand)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescrition)
                .hasFieldOrPropertyWithValue("active", expectedActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}
