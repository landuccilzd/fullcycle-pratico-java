package br.landucci.admin.catologo.domain.validation.handler;

import br.landucci.admin.catologo.domain.exception.DomainException;
import br.landucci.admin.catologo.domain.validation.Validation;
import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<ValidationError> errors;

    public Notification(List<ValidationError> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable t) {
        return new Notification(new ArrayList<>()).append(new ValidationError(t.getMessage()));
    }

    public static Notification create(final ValidationError error) {
        return new Notification(new ArrayList<>()).append(error);
    }

    @Override
    public Notification append(final ValidationError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation validation) {
        try {

        } catch (DomainException e) {
            this.errors.addAll(e.getErrors());
        } catch (Throwable t) {
            this.errors.add(new ValidationError(t.getMessage()));
        }

        return this;
    }

    @Override
    public List<ValidationError> getErrors() {
        return this.errors;
    }
}
