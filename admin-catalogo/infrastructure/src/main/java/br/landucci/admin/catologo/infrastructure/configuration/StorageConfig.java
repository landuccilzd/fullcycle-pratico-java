package br.landucci.admin.catologo.infrastructure.configuration;

import br.landucci.admin.catologo.infrastructure.configuration.properties.storage.StorageProperties;
import br.landucci.admin.catologo.infrastructure.services.StorageService;
import br.landucci.admin.catologo.infrastructure.services.impl.LocalDiskStorageService;
import br.landucci.admin.catologo.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties("storage.catalogo-videos") //Busca configurações no application.yml
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean(name = "storageService")
    @Profile({"dev", "prd"})
    public StorageService localDiskStorageService(final StorageProperties storageProperties) {
        return new LocalDiskStorageService(storageProperties.getLocalStoragePath());
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean //Se não criou nenhum bean anteriormente chama esse
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
