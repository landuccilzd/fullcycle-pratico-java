package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class VideoMediaTest extends UnitTest {

    @Test
    void givenValidParams_whenCreatingVideoMedia_thenShouldReturnAnInstance() {
        final var expectedId = UUID.randomUUID().toString();
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/ac";
        final var expectedEncodedLocation = "/images/ac-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        final var video = VideoMedia.with(expectedId, expectedChecksum, expectedName, expectedRawLocation,
                expectedEncodedLocation, expectedStatus);

        Assertions.assertNotNull(video);
        Assertions.assertEquals(expectedId, video.getId());
        Assertions.assertEquals(expectedChecksum, video.getChecksum());
        Assertions.assertEquals(expectedName, video.getName());
        Assertions.assertEquals(expectedRawLocation, video.getRawLocation());
        Assertions.assertEquals(expectedEncodedLocation, video.getEncodedLocation());
        Assertions.assertEquals(expectedStatus, video.getStatus());
    }

    @Test
    void givenTwoVideosWithSameChecksumAndLocation_whenCallingEquals_thenShouldReturnTrue() {
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/images/ac";

        final var video1 = VideoMedia.with(expectedChecksum, "Random", expectedRawLocation);
        final var video2 = VideoMedia.with(expectedChecksum, "Simple", expectedRawLocation);

        Assertions.assertEquals(video1, video2);
        Assertions.assertNotSame(video1, video2);
    }

    @Test
    void givenInvalidParams_whenCreating_thenShouldReturnAnError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                VideoMedia.with(null, "131", "Random", "/videos", "/videos",
                        MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                VideoMedia.with("id", "abc", null, "/videos", "/videos",
                        MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                VideoMedia.with("id", "abc", "Random", null, "/videos",
                        MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                VideoMedia.with("id", "abc", "Random", "/videos", null,
                        MediaStatus.PENDING)
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                VideoMedia.with("id", "abc", "Random", "/videos", "/videos",
                        null)
        );
    }
}