package br.landucci.admin.catologo.domain;

import br.landucci.admin.catologo.domain.events.DomainEvent;
import br.landucci.admin.catologo.domain.events.DomainEventPublisher;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<I extends Identifier> {

    protected final I id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final I id) {
        this(id, null);
    }

    protected Entity(final I id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull(id, "ID should not be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }

    public abstract void validate(ValidationHandler handler);

    public I getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) {
            return;
        }

        getDomainEvents().forEach(publisher::publishEvent);
        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event == null) {
            return;
        }

        this.domainEvents.add(event);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
