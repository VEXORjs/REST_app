package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.ResourceAllocation;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ResourceAllocationRepository {
    ResourceAllocation save(ResourceAllocation allocation);
    Optional<ResourceAllocation> findById(String id);
    List<ResourceAllocation> findByClientId(String clientId);
    List<ResourceAllocation> findByResourceId(String resourceId);
    List<ResourceAllocation> findCurrentByClientId(String clientId, Date now);
    List<ResourceAllocation> findPastByClientId(String clientId, Date now);
    List<ResourceAllocation> findCurrentByResourceId(String resourceId, Date now);
    List<ResourceAllocation> findPastByResourceId(String resourceId, Date now);
    List<ResourceAllocation> findAll();
    void deleteById(String id);
    boolean hasOverlappingAllocations(String resourceId, Date startTime, Date endTime);
}
