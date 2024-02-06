package br.landucci.admin.catologo.domain.exception;

import br.landucci.admin.catologo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
