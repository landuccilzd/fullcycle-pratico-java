package br.landucci.admin.catologo.infrastructure.configuration;

import br.landucci.admin.catologo.infrastructure.configuration.annotations.VideoCreatedQueue;
import br.landucci.admin.catologo.infrastructure.configuration.properties.amqp.QueueProperties;
import br.landucci.admin.catologo.infrastructure.services.EventService;
import br.landucci.admin.catologo.infrastructure.services.impl.RabbitEventService;
import br.landucci.admin.catologo.infrastructure.services.local.InMemoryEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    @Profile({"dev"})
    EventService inMemoryVideoCreatedEventService() {
        return new InMemoryEventService();
    }

    @Bean
    @VideoCreatedQueue
    @ConditionalOnMissingBean
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties properties,
            final RabbitOperations operations) {

        return new RabbitEventService(properties.getExchange(), properties.getRoutingKey(), operations);
    }
}
