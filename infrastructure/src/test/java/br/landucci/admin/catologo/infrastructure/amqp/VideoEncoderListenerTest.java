package br.landucci.admin.catologo.infrastructure.amqp;

import br.landucci.admin.catologo.AmqpTest;
import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusInputCommand;
import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusUseCase;
import br.landucci.admin.catologo.domain.utils.IdUtils;
import br.landucci.admin.catologo.domain.video.MediaStatus;
import br.landucci.admin.catologo.infrastructure.configuration.annotations.VideoEncodedQueue;
import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.configuration.properties.amqp.QueueProperties;
import br.landucci.admin.catologo.infrastructure.video.models.VideoEncoderCompleted;
import br.landucci.admin.catologo.infrastructure.video.models.VideoEncoderError;
import br.landucci.admin.catologo.infrastructure.video.models.VideoMessage;
import br.landucci.admin.catologo.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AmqpTest
public class VideoEncoderListenerTest {

    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase updateMediaStatusUseCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties queueProperties;

    @Test
    public void givenErrorResult_whenCallingListener_thenShouldProcess() throws InterruptedException {
        final var expectedError = new VideoEncoderError(
                new VideoMessage("123", "abc"), "Video not found");

        final var expectedMessage = Json.writeValueAsString(expectedError);

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData = harness.getNextInvocationDataFor(
                VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var message = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    public void givenCompletedResult_whenCallingListener_thenShouldCallUseCase() throws InterruptedException {
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata = new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var result = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);
        final var expectedMessage = Json.writeValueAsString(result);

        doNothing().when(updateMediaStatusUseCase).execute(any());

        this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

        final var invocationData = harness.getNextInvocationDataFor(
                VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);

        final var commandCaptor = ArgumentCaptor.forClass(UpdateMediaStatusInputCommand.class);
        verify(updateMediaStatusUseCase).execute(commandCaptor.capture());

        final var actualCommand = commandCaptor.getValue();
        Assertions.assertEquals(expectedStatus, actualCommand.status());
        Assertions.assertEquals(expectedId, actualCommand.videoId());
        Assertions.assertEquals(expectedResourceId, actualCommand.resourceId());
        Assertions.assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        Assertions.assertEquals(expectedFilePath, actualCommand.filename());
    }
}
