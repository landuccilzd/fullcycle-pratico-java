package br.landucci.admin.catologo.infrastructure.genre.models;

import br.landucci.admin.catologo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
public class UpdateGenreRequestCommandTest {

    @Autowired
    private JacksonTester<UpdateGenreRequestCommand> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Darama";
        final var expectedActive = true;
        final var expectedCategories = List.of("456", "789");

        final var requestCommand = new UpdateGenreRequestCommand(expectedName, expectedActive, expectedCategories);
        final var responseJson = this.json.write(requestCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.is_active", expectedActive)
                .hasJsonPathValue("$.categories_id", expectedCategories);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Drama";
        final var expectedActive = true;
        final var expectedCategories = "456, 789";

        final var strJson = """
            {
                "name":"%s",
                "is_active":"%s",
                "categories_id":["%s"]
            }
        """.formatted(expectedName, expectedActive, expectedCategories);

        final var requestCommand = this.json.parse(strJson);

        Assertions.assertThat(requestCommand)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("active", expectedActive)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories));
    }

}