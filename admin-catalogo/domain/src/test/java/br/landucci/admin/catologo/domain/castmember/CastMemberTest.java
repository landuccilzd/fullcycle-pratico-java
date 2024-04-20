package br.landucci.admin.catologo.domain.castmember;

import br.landucci.admin.catologo.domain.UnitTest;
import br.landucci.admin.catologo.domain.exception.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CastMemberTest extends UnitTest {

    @Test
    void givenValidParams_whenCreatingANewCastMember_thenShouldInstantiateACastMember() {
        final var expectedName = "Peach";
        final var expectedType = CastMemberType.ACTOR;

        final var castMember = CastMember.newCastMember(expectedName, expectedType);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());
        Assertions.assertEquals(expectedName, castMember.getName());
        Assertions.assertEquals(expectedType, castMember.getType());
        Assertions.assertNotNull(castMember.getCreatedAt());
        Assertions.assertNotNull(castMember.getUpdatedAt());
        Assertions.assertEquals(castMember.getCreatedAt(), castMember.getUpdatedAt());
    }

    @Test
    void givenAnInvalidNullNameParam_whenCreatingANewCastMember_thenShouldReceiveANotification() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(null, expectedType)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAnInvalidEmptyNameParameter_whenCreatingANewCastMember_thenShouldReceiveANotification() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be empty";

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(" ", expectedType)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAInvalidLongNameParam_whenCreatingANewCastMember_thenShouldReceiveANotification() {
        final var expectedName = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et 
            dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex 
            ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu 
            fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.""";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(expectedName, expectedType)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAInvalidNullTypeParam_whenCreatingANewCastMember_thenShouldReceiveANotification() {
        final var expectedName = "Zelda";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> CastMember.newCastMember(expectedName, null)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAValidCastMember_whenUpdatingACastMember_thenShouldUpdateACastMember() {
        final var expectedName = "Zelda";
        final var expectedType = CastMemberType.ACTOR;
        final var castMember = CastMember.newCastMember("Peach", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());


        final var clone = CastMember.clone(castMember);
        clone.updateName(expectedName).updateType(expectedType);

        Assertions.assertEquals(castMember.getId(), clone.getId());
        Assertions.assertEquals(expectedName, clone.getName());
        Assertions.assertEquals(expectedType, clone.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), clone.getCreatedAt());
        Assertions.assertTrue(castMember.getUpdatedAt().isBefore(clone.getUpdatedAt()));
    }

    @Test
    void givenAValidCastMember_whenUpdatingWithAnInvalidNullName_thenShouldReceiveNotification() {
        final var expectedName = "Zelda";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be null";

        final var castMember = CastMember.newCastMember(expectedName, CastMemberType.DIRECTOR);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> castMember.updateName(null)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAValidCastMember_whenUpdatingWithAnInvalidEmptyName_thenShouldReceiveANotification() {
        final var expectedName = "Zelda";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name should not be empty";

        final var castMember = CastMember.newCastMember(expectedName, CastMemberType.DIRECTOR);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> castMember.updateName(" ")
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAValidCastMember_whenUpdatingWithAnInvalidLongName_shouldReceiveNotification() {
        final var expectedName = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et 
            dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex 
            ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu 
            fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt 
            mollit anim id est laborum.""";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";

        final var castMember = CastMember.newCastMember("Zelda", expectedType);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> castMember.updateName(expectedName)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAValidCastMember_whenUpdatingWithAnInvalidShortName_shouldReceiveNotification() {
        final var expectedName = "Oi";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must have between 3 and 255 characters";

        final var castMember = CastMember.newCastMember("Zelda", expectedType);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> castMember.updateName(expectedName)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

    @Test
    void givenAValidCastMember_whenUpdatingWithAnInvalidNullType_thenShouldReceiveNotification() {
        final var expectedName = "Zelda";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Type should not be null";

        final var castMember = CastMember.newCastMember(expectedName, CastMemberType.DIRECTOR);

        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        final var exception = Assertions.assertThrows(
                NotificationException.class, () -> castMember.updateType(null)
        );

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(expectedErrorCount, exception.errorCount());
        Assertions.assertEquals(expectedErrorMessage, exception.firstError().message());
    }

}