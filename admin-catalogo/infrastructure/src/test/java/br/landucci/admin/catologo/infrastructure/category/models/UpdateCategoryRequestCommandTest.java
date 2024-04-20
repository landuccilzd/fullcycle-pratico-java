package br.landucci.admin.catologo.infrastructure.category.models;

import br.landucci.admin.catologo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class UpdateCategoryRequestCommandTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequestCommand> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedName = "Ficção Científica";
        final var expectedDescription = "Filmes de ficção científica";
        final var expectedActive = true;

        final var requestCommand = new UpdateCategoryRequestCommand(expectedName, expectedDescription, expectedActive);
        final var responseJson = this.json.write(requestCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedActive);
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedName = "Ficção Científica";
        final var expectedDescrition = "Filmes de ficção científica";
        final var expectedActive = true;

        final var strJson = """
            {
                "name":"%s",
                "description":"%s",
                "is_active":"%s"
            }
        """.formatted(expectedName, expectedDescrition, expectedActive);

        final var requestCommand = this.json.parse(strJson);

        Assertions.assertThat(requestCommand)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescrition)
                .hasFieldOrPropertyWithValue("active", expectedActive);
    }

}