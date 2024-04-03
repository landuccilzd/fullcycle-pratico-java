package br.landucci.admin.catologo.domain.castmember;

import br.landucci.admin.catologo.domain.validation.ValidationError;
import br.landucci.admin.catologo.domain.validation.ValidationHandler;
import br.landucci.admin.catologo.domain.validation.Validator;

public class CastMemberValidator extends Validator {
    private static final int MIN_NAME_LENGTH = 3;
    private static final int MAX_NAME_LENGTH = 255;

    private final CastMember castMember;

    public CastMemberValidator(final CastMember castMember, final ValidationHandler handler) {
        super(handler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        this.checkNameConstraints();
        this.checkTypeCopnstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
        if (name == null) {
            this.validationHandler().append(new ValidationError("Name should not be null"));
            return;
        }
        if (name.trim().isEmpty()) {
            this.validationHandler().append(new ValidationError("Name should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length < MIN_NAME_LENGTH || length > MAX_NAME_LENGTH) {
            this.validationHandler().append(new ValidationError("Name must have between 3 and 255 characters"));
        }
    }

    private void checkTypeCopnstraints() {
        final var type = this.castMember.getType();
        if (type == null) {
            this.validationHandler().append(new ValidationError("Type should not be null"));
            return;
        }
    }
}
