package br.landucci.admin.catologo.domain;

import br.landucci.admin.catologo.domain.events.DomainEvent;
import br.landucci.admin.catologo.domain.utils.IdUtils;
import br.landucci.admin.catologo.domain.utils.InstantUtils;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class EntityTest extends UnitTest {

    @Test
    void givenNullAsEvents_whenInstantiating_thenShouldBeOK() {
        final var entity = new DummyEntity(new DummyID(), null);

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertTrue(entity.getDomainEvents().isEmpty());
    }

    @Test
    void givenDomainEvents_whenPassingInConstructor_thenShouldCreateADefensiveClone() {
        final List<DomainEvent> events = new ArrayList<>();
        events.add(new DummyEvent());

        final var entity = new DummyEntity(new DummyID(), events);

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(1, entity.getDomainEvents().size());

        final var actualEvents = entity.getDomainEvents();
        Assertions.assertThrows(RuntimeException.class, () -> actualEvents.add(new DummyEvent()) );
    }

    @Test
    void givenEmptyDomainEvents_whenCallingRegisterEvent_thenShouldAddEventToList() {
        final var expectedEvents = 1;
        final var entity = new DummyEntity(new DummyID(), new ArrayList<>());

        entity.registerEvent(new DummyEvent());

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, entity.getDomainEvents().size());
    }

    @Test
    void givenAFewDomainEvents_whenCallingPublishEvents_thenShouldCallPublisherAndClearTheList() {
        final var expectedEvents = 0;
        final var expectedSentEvents = 2;
        final var counter = new AtomicInteger(0);
        final var entity = new DummyEntity(new DummyID(), new ArrayList<>());

        entity.registerEvent(new DummyEvent());
        entity.registerEvent(new DummyEvent());

        Assertions.assertEquals(2, entity.getDomainEvents().size());

        entity.publishDomainEvents(event -> {
            counter.incrementAndGet();
        });

        Assertions.assertNotNull(entity.getDomainEvents());
        Assertions.assertEquals(expectedEvents, entity.getDomainEvents().size());
        Assertions.assertEquals(expectedSentEvents, counter.get());
    }

    public static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {
        private final String id;

        public DummyID() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    public static class DummyEntity extends Entity<DummyID> {

        public DummyEntity(DummyID dummyID, List<DomainEvent> domainEvents) {
            super(dummyID, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {}
    }
}
