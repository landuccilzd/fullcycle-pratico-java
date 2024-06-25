package br.landucci.admin.catologo.infrastructure.configuration.properties.google;

public class GoogleStorageProperties {
    private String bucket;
    private int connectTimeout;
    private int readTimeout;
    private int retryDelay;
    private int retryMaxDelay;
    private int retryMaxAttempts;
    private double retryMultiplier;

    public GoogleStorageProperties() {}

    public String getBucket() {
        return bucket;
    }
    public int getConnectTimeout() {
        return connectTimeout;
    }
    public int getReadTimeout() {
        return readTimeout;
    }
    public int getRetryMaxAttempts() {
        return retryMaxAttempts;
    }
    public double getRetryMultiplier() {
        return retryMultiplier;
    }
    public int getRetryDelay() {
        return retryDelay;
    }
    public int getRetryMaxDelay() {
        return retryMaxDelay;
    }

    public GoogleStorageProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    public GoogleStorageProperties setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
    public GoogleStorageProperties setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
    public GoogleStorageProperties setRetryMaxAttempts(int retryMaxAttempts) {
        this.retryMaxAttempts = retryMaxAttempts;
        return this;
    }
    public GoogleStorageProperties setRetryMultiplier(double retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
        return this;
    }

    public GoogleStorageProperties setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }
    public GoogleStorageProperties setRetryMaxDelay(int retryMaxDelay) {
        this.retryMaxDelay = retryMaxDelay;
        return this;
    }

}