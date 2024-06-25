package br.landucci.admin.catologo.infrastructure.amqp;

import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusInputCommand;
import br.landucci.admin.catologo.application.video.media.update.UpdateMediaStatusUseCase;
import br.landucci.admin.catologo.domain.video.MediaStatus;
import br.landucci.admin.catologo.infrastructure.configuration.json.Json;
import br.landucci.admin.catologo.infrastructure.video.models.VideoEncoderCompleted;
import br.landucci.admin.catologo.infrastructure.video.models.VideoEncoderError;
import br.landucci.admin.catologo.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);

    static final String LISTENER_ID = "videoEncodedListener";

    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var result = Json.readValue(message, VideoEncoderResult.class);

        if (result instanceof VideoEncoderCompleted dto) {
            log.error("[message:video.listener.income] [status:completed] [payload:{}]", message);
            alterarSituacaoMediaCompleted(dto);
        } else if (result instanceof VideoEncoderError) {
            log.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        } else {
            log.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
        }
    }

    private void alterarSituacaoMediaCompleted(final VideoEncoderCompleted dto) {
        final var command = UpdateMediaStatusInputCommand.with(MediaStatus.COMPLETED, dto.id(),
                dto.video().resourceId(), dto.video().encodedVideoFolder(), dto.video().filePath());
        this.updateMediaStatusUseCase.execute(command);
    }

}
