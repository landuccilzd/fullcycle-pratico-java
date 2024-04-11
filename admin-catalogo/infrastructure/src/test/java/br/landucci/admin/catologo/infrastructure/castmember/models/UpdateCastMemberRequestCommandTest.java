package br.landucci.admin.catologo.infrastructure.castmember.models;

import br.landucci.admin.catologo.JacksonTest;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.infrastructure.castmember.model.UpdateCastMemberRequestCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateCastMemberRequestCommandTest {

    @Autowired
    private JacksonTester<UpdateCastMemberRequestCommand> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;

        final var requestCommand = new UpdateCastMemberRequestCommand(expectedName, expectedType);
        final var responseJson = this.json.write(requestCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;

        final var strJson = """
            {
                "name":"%s",
                "type":"%s"
            }
        """.formatted(expectedName, expectedType);

        final var requestCommand = this.json.parse(strJson);

        Assertions.assertThat(requestCommand)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType);
    }

}