package br.landucci.admin.catologo;

import br.landucci.admin.catologo.infrastructure.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@ActiveProfiles("tst")
@ActiveProfiles("tst-int")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(MySQLCleanUpExtension.class)
public @interface IntegrationTest {
}
