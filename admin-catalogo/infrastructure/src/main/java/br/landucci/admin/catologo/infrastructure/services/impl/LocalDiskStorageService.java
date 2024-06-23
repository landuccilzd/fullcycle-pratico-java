package br.landucci.admin.catologo.infrastructure.services.impl;

import br.landucci.admin.catologo.domain.video.Resource;
import br.landucci.admin.catologo.infrastructure.services.StorageService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class LocalDiskStorageService implements StorageService {

    private final String caminhoStorage;
    private final File diretorioStorage;

    private final Logger logger = Logger.getLogger(getClass().getName());

    public LocalDiskStorageService(final String caminhoStorage) {
        this.caminhoStorage = Objects.requireNonNull(caminhoStorage);
        this.diretorioStorage = new File(caminhoStorage);
    }

    @Override
    public Optional<Resource> get(String id) {
        try {
            final var path = Paths.get(caminhoStorage + File.separator + id);
            final var content = Files.readString(path);
            return Optional.ofNullable(Resource.with(content.getBytes(StandardCharsets.UTF_8), "123",
                    "Video", id));
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<String> list(String prefix) {
        final var retorno = new ArrayList<String>();

        for (final File file : this.diretorioStorage.listFiles()) {
            if (file.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
                retorno.add(file.getName());
            }
        }
        return retorno;
    }

    @Override
    public void store(String id, Resource resource) {
        final var arquivo = new File(diretorioStorage, id);
        if (arquivo.exists()) {
            return;
        }

        final var destination = new File(diretorioStorage.getAbsolutePath() + "/" + id.substring(0, id.indexOf("/")));
        if (!destination.exists()) {
            destination.mkdirs();
        }

        final var content = resource.getContent();
        try (final var fos = new FileOutputStream(arquivo.getAbsolutePath())) {
            fos.write(content);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void deleteAll(List<String> ids) {
        for (final File file : this.diretorioStorage.listFiles()) {
            if (ids.contains(file.getName())) {
                try {
                    Files.delete(Paths.get(file.getAbsolutePath()));
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                }
            }
        }
    }
}
