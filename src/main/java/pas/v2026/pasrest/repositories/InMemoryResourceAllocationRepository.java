package pas.v2026.pasrest.repositories;

import pas.v2026.pasrest.model.ResourceAllocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryResourceAllocationRepository implements ResourceAllocationRepository {
    private final Map<String, ResourceAllocation> allocations = new ConcurrentHashMap<>();

    @Override
    public synchronized ResourceAllocation save(ResourceAllocation allocation) {
        if (allocation.getId() == null) {
            allocation.setId(UUID.randomUUID().toString());
        }
        allocations.put(allocation.getId(), allocation);
        return allocation;
    }

    @Override
    public Optional<ResourceAllocation> findById(String id) {
        return Optional.ofNullable(allocations.get(id));
    }

    @Override
    public List<ResourceAllocation> findByClientId(String clientId) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findByResourceId(String resourceId) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findCurrentByClientId(String clientId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .filter(a -> a.getEndTime().after(now))
                .collect(Collectors.toList());
    }


    @Override
    public List<ResourceAllocation> findPastByClientId(String clientId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .filter(a -> !a.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findCurrentByResourceId(String resourceId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .filter(a -> a.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findPastByResourceId(String resourceId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .filter(a -> !a.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findAll() {
        return new ArrayList<>(allocations.values());
    }

    @Override
    public synchronized void deleteById(String id) {
        allocations.remove(id);
    }

    @Override
    public boolean hasOverlappingAllocations(String resourceId, Date startTime, Date endTime) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .anyMatch(a -> a.getStartTime().before(endTime) && a.getEndTime().after(startTime));
    }
}
