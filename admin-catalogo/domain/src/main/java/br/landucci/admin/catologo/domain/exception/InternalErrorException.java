package br.landucci.admin.catologo.domain.exception;

import br.landucci.admin.catologo.domain.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class InternalErrorException extends NoStacktraceException {

    private final transient List<ValidationError> errors;

    protected InternalErrorException(final String aMessage, final Throwable t) {
        super(aMessage, t);
        this.errors = new ArrayList();
    }

    protected InternalErrorException(final String message, final List<ValidationError> errors) {
        super(message);
        this.errors = errors;
    }

    public static InternalErrorException with(final String message, final Throwable t) {
        return new InternalErrorException(message, t);
    }

    public static DomainException with(final ValidationError error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<ValidationError> errors) {
        return new DomainException("", errors);
    }

    @Override
    List<ValidationError> getErrors() {
        return errors;
    }


}
