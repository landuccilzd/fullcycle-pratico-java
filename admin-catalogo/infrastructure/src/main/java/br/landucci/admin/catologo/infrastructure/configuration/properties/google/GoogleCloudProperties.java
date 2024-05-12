package br.landucci.admin.catologo.infrastructure.configuration.properties.google;

public class GoogleCloudProperties {

    private String credentials;
    private String projectId;

    public GoogleCloudProperties() {}

    public String getCredentials() {
        return credentials;
    }
    public String getProjectId() {
        return projectId;
    }

    public GoogleCloudProperties setCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }
    public GoogleCloudProperties setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

}