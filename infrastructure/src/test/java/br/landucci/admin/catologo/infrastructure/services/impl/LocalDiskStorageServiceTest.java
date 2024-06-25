package br.landucci.admin.catologo.infrastructure.services.impl;

import br.landucci.admin.catologo.domain.video.Resource;
import br.landucci.admin.catologo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

class LocalDiskStorageServiceTest {

    private LocalDiskStorageService service;

//    @BeforeEach
    void setUp() {
        this.service = new LocalDiskStorageService("C:\\dev\\storage\\tst");
    }

//    @Test
    void givenAValidResource_whenGetting_thenShouldRetrieveIt() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.store(expectedName, expectedResource);
        final var resource = service.get(expectedName).get();

        Assertions.assertEquals(expectedResource.getName(), resource.getName());
    }

//    @Test
    void givenAnInvalidResource_whenGetting_thenShouldRetrieveEmpty() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.store(expectedName, expectedResource);
        final var resource = service.get("nome-nao-existente");

        Assertions.assertTrue(resource.isEmpty());
    }

//    @Test
    void givenAValidPrefix_whenListing_thenShouldRetrieveAll() {
        final var expectedName1 = "Item 1";
        final var expectecContent1 = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum1 = UUID.randomUUID().toString();
        final var expectedContentType1 = VideoMediaType.THUMBNAIL;
        final var expectedResource1 = Resource.with(expectecContent1, expectedChecksum1, expectedContentType1.name(),
                expectedName1);

        final var expectedName2 = "Item 2";
        final var expectecContent2 = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum2 = UUID.randomUUID().toString();
        final var expectedContentType2 = VideoMediaType.THUMBNAIL;
        final var expectedResource2 = Resource.with(expectecContent2, expectedChecksum2, expectedContentType2.name(),
                expectedName2);

        this.service.store(expectedName1, expectedResource1);
        this.service.store(expectedName2, expectedResource2);

        final var resources = service.list("it");

        Assertions.assertEquals(expectedResource1.getName(), resources.get(0));
        Assertions.assertEquals(expectedResource2.getName(), resources.get(1));
    }

//    @Test
    void givenAValidResource_whenStoring_thenShouldStoreIt() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.store(expectedName, expectedResource);
        final var resource = this.service.get(expectedName).get();

        Assertions.assertEquals(expectedResource.getName(), resource.getName());
    }

//    @Test
    void givenAResource_whenDeleting_thenShouldDelete() {
        final var expectedName1 = "Item 1";
        final var expectecContent1 = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum1 = UUID.randomUUID().toString();
        final var expectedContentType1 = VideoMediaType.THUMBNAIL;
        final var expectedResource1 = Resource.with(expectecContent1, expectedChecksum1, expectedContentType1.name(),
                expectedName1);

        final var expectedName2 = "Item 2";
        final var expectecContent2 = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum2 = UUID.randomUUID().toString();
        final var expectedContentType2 = VideoMediaType.THUMBNAIL;
        final var expectedResource2 = Resource.with(expectecContent2, expectedChecksum2, expectedContentType2.name(),
                expectedName2);

        this.service.store(expectedName1, expectedResource1);
        this.service.store(expectedName2, expectedResource2);

        final var expectedIds = List.of(expectedName1);
        service.deleteAll(expectedIds);

        final var count = this.service.list("it").size();
        Assertions.assertEquals(1, count);
    }

}