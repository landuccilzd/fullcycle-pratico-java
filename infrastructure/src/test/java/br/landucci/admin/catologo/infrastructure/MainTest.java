package br.landucci.admin.catologo.infrastructure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

class MainTest {
    @Test
    void testeMain() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "tst");
        Assertions.assertNotNull(new Main());
        Main.main(new String[] {});
    }
}
