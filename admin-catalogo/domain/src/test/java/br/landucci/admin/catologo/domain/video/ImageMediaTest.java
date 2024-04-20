package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageMediaTest extends UnitTest {

    @Test
    void givenValidParams_whenCreatingNewImage_thenShouldReturnAnInstance() {
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/ac";

        final var image = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        Assertions.assertNotNull(image);
        Assertions.assertEquals(expectedChecksum, image.getChecksum());
        Assertions.assertEquals(expectedName, image.getName());
        Assertions.assertEquals(expectedLocation, image.getLocation());
    }

    @Test
    void givenTwoImagesWithSameChecksumAndLocation_whenCallingEquals_thenShouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/ac";

        final var image1 = ImageMedia.with(expectedChecksum, "Random", expectedLocation);
        final var image2 = ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        Assertions.assertEquals(image1, image2);
        Assertions.assertNotSame(image1, image2);
    }

    @Test
    void givenInvalidParams_whenCreating_thenShouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with(null, "Random", "/images")
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", null, "/images")
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", "Random", null)
        );
    }

}