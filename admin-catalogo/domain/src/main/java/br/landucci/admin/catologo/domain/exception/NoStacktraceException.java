package br.landucci.admin.catologo.domain.exception;

import br.landucci.admin.catologo.domain.validation.ValidationError;

import java.util.List;

public abstract class NoStacktraceException extends RuntimeException {

    public NoStacktraceException(final String message) {
        super(message, null);
    }

    public NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }

    public boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    public ValidationError firstError() {
        return getErrors().get(0);
    }

    public int errorCount() {
        return !hasErrors() ? 0 : getErrors().size();
    }

    abstract List<ValidationError> getErrors();
}
