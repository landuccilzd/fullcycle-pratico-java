package br.landucci.admin.catologo.infrastructure.services.impl;

import br.landucci.admin.catologo.AmqpTest;
import br.landucci.admin.catologo.domain.video.VideoMediaCreated;
import br.landucci.admin.catologo.infrastructure.configuration.annotations.VideoCreatedQueue;
import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.services.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@AmqpTest
class RabbitEventServiceTest {

    private static final String LISTENER = "video.created.tst-int";

    @Autowired
    @VideoCreatedQueue
    private EventService publisher;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {
        final var notification = new VideoMediaCreated("resource", "filepath");
        final var expectedMessage = Json.writeValueAsString(notification);

        this.publisher.send(notification);

        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var message = (String) invocationData.getArguments()[0];

        Assertions.assertNotNull(message);
    }

    @Component
    static class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER, queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message) {
        }
    }
}
