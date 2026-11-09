package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.Resource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepository {
    Resource save(Resource resource);
    Optional<Resource> findById(UUID id);
    List<Resource> findAll();
    List<Resource> findByNamePartial(String namePart);
    List<Resource> findByAvailable(boolean available);
    void deleteById (UUID id);
}
