package br.landucci.admin.catologo.infrastructure.services.impl;

import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.services.EventService;
import org.springframework.amqp.rabbit.core.RabbitOperations;

import java.util.Objects;

public class RabbitEventService implements EventService {

    private final String exchange;
    private final String routingKey;
    private final RabbitOperations operations;

    public RabbitEventService(
            final String exchange,
            final String routingKey,
            final RabbitOperations operations
    ) {
        this.exchange = Objects.requireNonNull(exchange);
        this.routingKey = Objects.requireNonNull(routingKey);
        this.operations = Objects.requireNonNull(operations);
    }

    @Override
    public void send(final Object event) {
        this.operations.convertAndSend(this.exchange, this.routingKey, Json.writeValueAsString(event));
    }
}
