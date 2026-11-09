package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.Resource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryResourceRepository implements ResourceRepository{
    private final Map<UUID, Resource> resources = new ConcurrentHashMap<>();

    @Override
    public Resource save(Resource resource) {
        if (resource.getId() == null) {
            resource.setId(UUID.randomUUID());
        }
        resources.put(resource.getId(), resource);
        return resource;
    }

    @Override
    public Optional<Resource> findById(UUID id) {
        return Optional.ofNullable(resources.get(id));
    }

    @Override
    public List<Resource> findAll() {
        return new ArrayList<>(resources.values());
    }

    @Override
    public List<Resource> findByNamePartial(String namePart) {
        return resources.values().stream()
                .filter(r -> r.getName().toLowerCase().contains(namePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Resource> findByAvailable(boolean available) {
        return resources.values().stream()
                .filter(r -> r.isAvailable() == available)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        resources.remove(id);
    }
}
