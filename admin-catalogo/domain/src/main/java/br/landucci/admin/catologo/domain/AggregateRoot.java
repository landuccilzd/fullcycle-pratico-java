package br.landucci.admin.catologo.domain;


import br.landucci.admin.catologo.domain.events.DomainEvent;

import java.util.List;

public abstract class AggregateRoot<I extends Identifier> extends Entity<I> {

    protected AggregateRoot(final I id) {
        super(id);
    }

    protected AggregateRoot(final I id, final List<DomainEvent> events) {
        super(id, events);
    }


}