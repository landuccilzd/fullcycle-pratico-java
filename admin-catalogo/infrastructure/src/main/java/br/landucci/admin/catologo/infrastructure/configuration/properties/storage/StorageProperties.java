package br.landucci.admin.catologo.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(StorageProperties.class);

    private String localStoragePath;
    private String locationPattern;
    private String filenamePattern;

    public StorageProperties() {}

    public String getLocalStoragePath() {
        return localStoragePath;
    }
    public String getLocationPattern() {
        return locationPattern;
    }
    public String getFilenamePattern() {
        return filenamePattern;
    }

    public StorageProperties setLocalStoragePath(String localStoragePath) {
        this.localStoragePath = localStoragePath;
        return this;
    }
    public StorageProperties setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
        return this;
    }
    public StorageProperties setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
        return this;
    }

    @Override
    public void afterPropertiesSet() {
        log.info(this.toString());
    }

    @Override
    public String toString() {
        return " StorageProperties { localStoragePath: %s, locationPattern: %s, filenamePattern: %s }"
                .formatted(localStoragePath, locationPattern, filenamePattern);
    }
}
