package br.landucci.admin.catologo.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {

    @Test
    public void testCreateUseCase() {
        var useCase = new UseCase();
        Assertions.assertNotNull(useCase);
        Assertions.assertNotNull(useCase.execute());
    }
}
