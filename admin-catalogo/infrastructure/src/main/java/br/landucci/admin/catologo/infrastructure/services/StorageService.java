package br.landucci.admin.catologo.infrastructure.services;

import br.landucci.admin.catologo.domain.video.Resource;

import java.util.List;
import java.util.Optional;

public interface StorageService {

    Optional<Resource> get(String id);
    List<String> list(String prefix);
    void store(String id, Resource resource);
    void deleteAll(final List<String> ids);
}
