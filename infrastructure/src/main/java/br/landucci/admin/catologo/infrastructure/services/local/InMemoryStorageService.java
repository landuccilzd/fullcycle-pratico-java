package br.landucci.admin.catologo.infrastructure.services.local;

import br.landucci.admin.catologo.domain.video.Resource;
import br.landucci.admin.catologo.infrastructure.services.StorageService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {

    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public void clear() {
        this.storage.clear();
    }

    public Map<String, Resource> getStorage() {
        return this.storage;
    }

    @Override
    public Optional<Resource> get(final String id) {
        return Optional.ofNullable(this.storage.get(id));
    }

    @Override
    public List<String> list(final String prefix) {
        return this.storage.keySet().stream()
                .filter(it -> it.toLowerCase().startsWith(prefix.toLowerCase()))
                .toList();
    }

    @Override
    public void store(final String id, final Resource resource) {
        this.storage.put(id, resource);
    }

    @Override
    public void deleteAll(final List<String> ids) {
        ids.forEach(this.storage::remove);
    }
}
