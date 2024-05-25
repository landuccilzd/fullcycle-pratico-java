package br.landucci.admin.catologo;

import br.landucci.admin.catologo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("tst-int")
@SpringBootTest(classes = WebServerConfig.class)
public @interface AmqpTest {
}
