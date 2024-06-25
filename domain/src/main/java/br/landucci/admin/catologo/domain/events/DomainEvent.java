package br.landucci.admin.catologo.domain.events;

import java.io.Serializable;
import java.time.Instant;

public interface DomainEvent extends Serializable {
    Instant occurredOn();
}
