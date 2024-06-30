package br.landucci.admin.catologo.infrastructure;

import br.landucci.admin.catologo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "dev");
//        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "sbx");
        SpringApplication.run(WebServerConfig.class, args);
    }
}