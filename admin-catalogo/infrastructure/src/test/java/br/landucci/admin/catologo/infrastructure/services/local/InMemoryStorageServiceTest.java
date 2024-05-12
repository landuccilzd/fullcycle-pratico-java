package br.landucci.admin.catologo.infrastructure.services.local;

import br.landucci.admin.catologo.domain.video.Resource;
import br.landucci.admin.catologo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class InMemoryStorageServiceTest {

    private InMemoryStorageService service = new InMemoryStorageService();

    @BeforeEach
    void setUp() {
        service.clear();
    }
    @Test
    void givenAValidResource_whenGetting_thenShouldRetrieveIt() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.getStorage().put(expectedName, expectedResource);

        final var resource = service.get(expectedName).get();

        Assertions.assertEquals(expectedResource.getName(), resource.getName());
        Assertions.assertEquals(expectedResource.getContent(), resource.getContent());
        Assertions.assertEquals(expectedResource.getChecksum(), resource.getChecksum());
        Assertions.assertEquals(expectedResource.getContentType(), resource.getContentType());
    }

    @Test
    void givenAnInvalidResource_whenGetting_thenShouldRetrieveEmpty() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.getStorage().put(expectedName, expectedResource);

        final var resource = service.get("nome-nao-existente");

        Assertions.assertTrue(resource.isEmpty());
    }

    @Test
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

        this.service.getStorage().put(expectedName1, expectedResource1);
        this.service.getStorage().put(expectedName2, expectedResource2);

        final var resources = service.list("it");

        Assertions.assertEquals(expectedResource1.getName(), resources.get(1));
        Assertions.assertEquals(expectedResource2.getName(), resources.get(0));
    }

    @Test
    void givenAValidResource_whenStoring_thenShouldStoreIt() {
        final var expectedName = UUID.randomUUID().toString();
        final var expectecContent = UUID.randomUUID().toString().getBytes();
        final var expectedChecksum = UUID.randomUUID().toString();
        final var expectedContentType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Resource.with(expectecContent, expectedChecksum, expectedContentType.name(),
                expectedName);

        this.service.store(expectedName, expectedResource);
        final var resource = this.service.getStorage().get(expectedName);

        Assertions.assertEquals(expectedResource.getName(), resource.getName());
        Assertions.assertEquals(expectedResource.getContent(), resource.getContent());
        Assertions.assertEquals(expectedResource.getChecksum(), resource.getChecksum());
        Assertions.assertEquals(expectedResource.getContentType(), resource.getContentType());
    }

    @Test
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

        this.service.getStorage().put("item1", expectedResource1);
        this.service.getStorage().put("item2", expectedResource2);

        final var expectedIds = List.of("item1");
        service.deleteAll(expectedIds);

        final var count = this.service.list("it").size();
        Assertions.assertEquals(1, count);
    }

}