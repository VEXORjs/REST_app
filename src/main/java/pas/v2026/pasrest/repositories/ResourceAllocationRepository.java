package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.ResourceAllocation;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceAllocationRepository {
    ResourceAllocation save(ResourceAllocation allocation);
    Optional<ResourceAllocation> findById(UUID id);
    List<ResourceAllocation> findByClientId(UUID clientId);
    List<ResourceAllocation> findByResourceId(UUID resourceId);
    List<ResourceAllocation> findCurrentByClientId(UUID clientId, Date now);
    List<ResourceAllocation> findPastByClientId(UUID clientId, Date now);
    List<ResourceAllocation> findCurrentByResourceId(UUID resourceId, Date now);
    List<ResourceAllocation> findPastByResourceId(UUID resourceId, Date now);
    List<ResourceAllocation> findAll();
    void deleteById(UUID id);
}
