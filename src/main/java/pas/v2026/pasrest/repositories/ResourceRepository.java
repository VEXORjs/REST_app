package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.Resource;
import java.util.List;
import java.util.Optional;

public interface ResourceRepository {
    Resource save(Resource resource);
    Optional<Resource> findById(String id);
    List<Resource> findAll();
    List<Resource> findByNamePartial(String namePart);
    List<Resource> findByAvailable(boolean available);
    void deleteById (String id);
}
