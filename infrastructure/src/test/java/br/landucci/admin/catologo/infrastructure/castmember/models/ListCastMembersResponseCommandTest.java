package br.landucci.admin.catologo.infrastructure.castmember.models;

import br.landucci.admin.catologo.JacksonTest;
import br.landucci.admin.catologo.domain.castmember.CastMemberType;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.infrastructure.castmember.model.ListCastMembersResponseCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
class ListCastMembersResponseCommandTest {

    @Autowired
    private JacksonTester<ListCastMembersResponseCommand> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var responseCommand = new ListCastMembersResponseCommand(expectedId, expectedName, expectedType,
                expectedCreatedAt, expectedUpdatedAt);

        final var responseJson = this.json.write(responseCommand);

        Assertions.assertThat(responseJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
    }

    @Test
    void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        final var strJson = """
            {
                "id":"%s",
                "name":"%s",
                "type":"%s",
                "created_at":"%s",
                "updated_at":"%s"
            }
        """.formatted(expectedId, expectedName, expectedType, expectedCreatedAt, expectedUpdatedAt);

        final var responseCommand = this.json.parse(strJson);

        Assertions.assertThat(responseCommand)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("type", expectedType)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
    }
}
