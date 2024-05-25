package br.landucci.admin.catologo.infrastructure.configuration;

import br.landucci.admin.catologo.infrastructure.configuration.annotations.VideoCreatedQueue;
import br.landucci.admin.catologo.infrastructure.configuration.properties.amqp.QueueProperties;
import br.landucci.admin.catologo.infrastructure.services.EventService;
import br.landucci.admin.catologo.infrastructure.services.impl.RabbitEventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {

    @Bean
    @VideoCreatedQueue
    EventService videoCreatedEventService(
            @VideoCreatedQueue final QueueProperties properties,
            final RabbitOperations operations) {

        return new RabbitEventService(properties.getExchange(), properties.getRoutingKey(), operations);
    }
}
