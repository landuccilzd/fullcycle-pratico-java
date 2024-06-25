package br.landucci.admin.catologo.infrastructure.video;

import br.landucci.admin.catologo.IntegrationTest;
import br.landucci.admin.catologo.domain.video.*;
import br.landucci.admin.catologo.infrastructure.services.StorageService;
import br.landucci.admin.catologo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway gateway;

    @Autowired
    private StorageService service;

    @BeforeEach
    void setUp() {
        getStorageService().clear();
    }

    @Test
    void testInjection() {
        Assertions.assertNotNull(this.gateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, this.gateway);

        Assertions.assertNotNull(this.service);
        Assertions.assertInstanceOf(InMemoryStorageService.class, this.service);
    }

    @Test
    void givenAValidVideoId_whenGettingResource_thenShouldReturnIt() {
        final var expectedVideoId = VideoID.unique();
        final var expectedVideoName = UUID.randomUUID().toString();
        final var expectecVideoContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedVideoChecksum = UUID.randomUUID().toString();
        final var expectedVideoContentType = VideoMediaType.VIDEO;
        final var expectedVideoResource = Resource.with(expectecVideoContent, expectedVideoChecksum,
                expectedVideoContentType.name(), expectedVideoName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedVideoId.getValue(),
                VideoMediaType.VIDEO.name()), expectedVideoResource);

        final var expectedTrailerId = VideoID.unique();
        final var expectedTrailerName = UUID.randomUUID().toString();
        final var expectecTrailerContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedTrailerChecksum = UUID.randomUUID().toString();
        final var expectedTrailerContentType = VideoMediaType.TRAILER;
        final var expectedTrailerResource = Resource.with(expectecTrailerContent, expectedTrailerChecksum,
                expectedTrailerContentType.name(), expectedTrailerName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedTrailerId.getValue(),
                VideoMediaType.TRAILER.name()), expectedTrailerResource);

        final var expectedBannerId = VideoID.unique();
        final var expectedBannerName = UUID.randomUUID().toString();
        final var expectecBannerContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedBannerChecksum = UUID.randomUUID().toString();
        final var expectedBannerContentType = VideoMediaType.TRAILER;
        final var expectedBannerResource = Resource.with(expectecBannerContent, expectedBannerChecksum,
                expectedBannerContentType.name(), expectedBannerName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedBannerId.getValue(),
                VideoMediaType.BANNER.name()), expectedBannerResource);

        Assertions.assertEquals(3, getStorageService().getStorage().size());

        final var resource = this.gateway.getResource(expectedVideoId, expectedVideoContentType).get();

        Assertions.assertEquals(expectedVideoResource, resource);
    }


    @Test
    void givenAnInvalidType_whenGettingResource_thenShouldReturnEmpty() {
        final var expectedVideoId = VideoID.unique();
        final var expectedVideoName = UUID.randomUUID().toString();
        final var expectecVideoContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedVideoChecksum = UUID.randomUUID().toString();
        final var expectedVideoContentType = VideoMediaType.VIDEO;
        final var expectedVideoResource = Resource.with(expectecVideoContent, expectedVideoChecksum,
                expectedVideoContentType.name(), expectedVideoName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedVideoId.getValue(),
                VideoMediaType.VIDEO.name()), expectedVideoResource);

        final var expectedTrailerId = VideoID.unique();
        final var expectedTrailerName = UUID.randomUUID().toString();
        final var expectecTrailerContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedTrailerChecksum = UUID.randomUUID().toString();
        final var expectedTrailerContentType = VideoMediaType.TRAILER;
        final var expectedTrailerResource = Resource.with(expectecTrailerContent, expectedTrailerChecksum,
                expectedTrailerContentType.name(), expectedTrailerName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedTrailerId.getValue(),
                VideoMediaType.TRAILER.name()), expectedTrailerResource);

        final var expectedBannerId = VideoID.unique();
        final var expectedBannerName = UUID.randomUUID().toString();
        final var expectecBannerContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedBannerChecksum = UUID.randomUUID().toString();
        final var expectedBannerContentType = VideoMediaType.BANNER;
        final var expectedBannerResource = Resource.with(expectecBannerContent, expectedBannerChecksum,
                expectedBannerContentType.name(), expectedBannerName);

        getStorageService().store("videoId-%s/type-%s".formatted(expectedBannerId.getValue(),
                VideoMediaType.BANNER.name()), expectedBannerResource);

        Assertions.assertEquals(3, getStorageService().getStorage().size());

        final var actualResult = this.gateway.getResource(expectedTrailerId, expectedBannerContentType);

        Assertions.assertTrue(actualResult.isEmpty());
    }

    @Test
    void givenAValidResource_whenStoragingVideo_thenShouldStoreIt() {
        final var expectedId = VideoID.unique();
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.VIDEO;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedId.getValue(), expectedContentType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        final var media = this.gateway.storeVideo(expectedId, VideoResource.with(expectedContentType, expectedResource));

        // then
        Assertions.assertNotNull(media.getId());
        Assertions.assertEquals(expectedLocation, media.getRawLocation());
        Assertions.assertEquals(expectedResource.getName(), media.getName());
        Assertions.assertEquals(expectedResource.getChecksum(), media.getChecksum());
        Assertions.assertEquals(expectedStatus, media.getStatus());
        Assertions.assertEquals(expectedEncodedLocation, media.getEncodedLocation());

        final var resource = getStorageService().getStorage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, resource);
    }

    @Test
    void givenAValidResource_whenStoringAnImage_thenShouldStoreIt() {
        final var expectedId = VideoID.unique();
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.BANNER;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedId.getValue(), expectedContentType.name());

        final var media = this.gateway.storeImage(expectedId, VideoResource.with(expectedContentType, expectedResource));

        Assertions.assertNotNull(media.getId());
        Assertions.assertEquals(expectedLocation, media.getLocation());
        Assertions.assertEquals(expectedResource.getName(), media.getName());
        Assertions.assertEquals(expectedResource.getChecksum(), media.getChecksum());

        final var resource = getStorageService().getStorage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, resource);
    }

    @Test
    void givenAValidVideoId_whenClearingResources_thenShouldDeleteAll() {
        final var toBeDeleted = new ArrayList<String>();
        final var expectedValues = new ArrayList<String>();

        final var voId = VideoID.unique();
        final var voName = UUID.randomUUID().toString();
        final var voContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var voChecksum = UUID.randomUUID().toString();

        final var videoOneVideo = Resource.with(voContent, voChecksum, VideoMediaType.VIDEO.name(), voName);
        final var videoOneVideoID = "videoId-%s/type-%s".formatted(voId.getValue(), VideoMediaType.VIDEO.name());
        toBeDeleted.add(videoOneVideoID);
        this.getStorageService().store(videoOneVideoID, videoOneVideo);

        final var videoOneBanner = Resource.with(voContent, voChecksum, VideoMediaType.BANNER.name(), voName);
        final var videoOneBannerID = "videoId-%s/type-%s".formatted(voId.getValue(), VideoMediaType.BANNER.name());
        toBeDeleted.add(videoOneBannerID);
        this.getStorageService().store(videoOneBannerID, videoOneBanner);

        final var videoOneTrailer = Resource.with(voContent, voChecksum, VideoMediaType.TRAILER.name(), voName);
        final var videoOneTrailerID = "videoId-%s/type-%s".formatted(voId.getValue(), VideoMediaType.TRAILER.name());
        toBeDeleted.add(videoOneTrailerID);
        this.getStorageService().store(videoOneTrailerID, videoOneTrailer);

        final var vtId = VideoID.unique();
        final var vtName = UUID.randomUUID().toString();
        final var vtContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var vtChecksum = UUID.randomUUID().toString();

        final var videoTwoVideo = Resource.with(vtContent, vtChecksum, VideoMediaType.VIDEO.name(), vtName);
        final var videoTwoVideoID = "videoId-%s/type-%s".formatted(vtId.getValue(), VideoMediaType.VIDEO.name());
        expectedValues.add(videoTwoVideoID);
        this.getStorageService().store(videoTwoVideoID, videoTwoVideo);

        final var videoTwoBanner = Resource.with(vtContent, vtChecksum, VideoMediaType.BANNER.name(), vtName);
        final var videoTwoBannerID = "videoId-%s/type-%s".formatted(vtId.getValue(), VideoMediaType.BANNER.name());
        expectedValues.add(videoTwoBannerID);
        getStorageService().store(videoTwoBannerID, videoTwoBanner);

        Assertions.assertEquals(5, getStorageService().getStorage().size());

        this.gateway.clearResources(voId);

        Assertions.assertEquals(2, getStorageService().getStorage().size());

        final var keys = getStorageService().getStorage().keySet();

        Assertions.assertEquals(expectedValues.size(), keys.size());
        Assertions.assertTrue(keys.containsAll(expectedValues));
    }

    private InMemoryStorageService getStorageService() {
        return (InMemoryStorageService) this.service;
    }
}