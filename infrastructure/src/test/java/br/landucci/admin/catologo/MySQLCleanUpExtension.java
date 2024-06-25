package br.landucci.admin.catologo;

import br.landucci.admin.catologo.infrastructure.castmember.persistence.CastMemberRepository;
import br.landucci.admin.catologo.infrastructure.category.persistence.CategoryRepository;
import br.landucci.admin.catologo.infrastructure.genre.persistence.GenreRepository;
import br.landucci.admin.catologo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
            appContext.getBean(VideoRepository.class),
            appContext.getBean(CastMemberRepository.class),
            appContext.getBean(GenreRepository.class),
            appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}
