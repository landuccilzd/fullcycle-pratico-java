package br.landucci.admin.catologo.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(ValidationError error);
    ValidationHandler append(ValidationHandler handler);
    <T> T validate(Validation<T> validation);

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default ValidationError firstError() {
        return (hasErrors()) ? getErrors().get(0) : null;
    }

    default int getErrorCount() {
        return !hasErrors() ? 0 : getErrors().size();
    }

    List<ValidationError> getErrors();
}
