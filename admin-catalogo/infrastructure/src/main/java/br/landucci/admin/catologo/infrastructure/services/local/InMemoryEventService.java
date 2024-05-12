package br.landucci.admin.catologo.infrastructure.services.local;

import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.services.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InMemoryEventService implements EventService {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryEventService.class);

    @Override
    public void send(Object event) {
        LOG.info("Event was observed: {}", Json.writeValueAsString(event));
    }
}
