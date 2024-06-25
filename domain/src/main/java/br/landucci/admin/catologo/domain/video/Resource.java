package br.landucci.admin.catologo.domain.video;

import br.landucci.admin.catologo.domain.ValueObject;

import java.util.Objects;

public class Resource extends ValueObject {

    private final byte[] content;
    private final String checksum;
    private final String contentType;
    private final String name;

    private Resource(final byte[] content, final String checksum, final String contentType, final String name) {
        this.content = Objects.requireNonNull(content);
        this.checksum = Objects.requireNonNull(checksum);
        this.contentType = Objects.requireNonNull(contentType);
        this.name = Objects.requireNonNull(name);
    }

    public static Resource with(final byte[] content, final String checksum, final String contentType,
            final String name) {
        return new Resource(content, checksum, contentType, name);
    }

    public byte[] getContent() {
        return content;
    }
    public String getChecksum() {
        return checksum;
    }
    public String getContentType() {
        return contentType;
    }
    public String getName() {
        return name;
    }
}
