package br.landucci.admin.catologo.infrastructure.genre;

import br.landucci.admin.catologo.MySQLGatewayTest;
import br.landucci.admin.catologo.infrastructure.category.CategoryMySQLGateway;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private GenreMySQLGateway gateway;
    @Autowired
    private GenreRepository repository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(gateway);
        Assertions.assertNotNull(repository);
    }
}
