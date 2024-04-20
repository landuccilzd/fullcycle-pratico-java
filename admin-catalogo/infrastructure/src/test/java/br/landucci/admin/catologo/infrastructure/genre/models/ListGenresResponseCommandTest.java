package br.landucci.admin.catologo.infrastructure.genre.models;

import br.landucci.admin.catologo.JacksonTest;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
class ListGenresResponseCommandTest {

    @Autowired
    private JacksonTester<ListGenresResponseCommand> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = List.of("456", "789");
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var responseCommand = new ListGenresResponseCommand(expectedId, expectedName, expectedActive,
                expectedCategories, expectedCreatedAt, expectedUpdatedAt, expectedDeletedAt);

        final var responseJson = this.json.write(responseCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedActive)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = "456, 789";
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();
        final var expectedDeletedAt = InstantUtils.now();

        final var strJson = """
            {
                "id":"%s",
                "name":"%s",
                "is_active":"%s",
                "categories_id":["%s"],
                "created_at":"%s",
                "updated_at":"%s",
                "deleted_at":"%s"
            }
        """.formatted(expectedId, expectedName, expectedActive, expectedCategories, expectedCreatedAt,
                expectedUpdatedAt, expectedDeletedAt);

        final var responseCommand = this.json.parse(strJson);

        Assertions.assertThat(responseCommand)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("active", expectedActive)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories))
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}
