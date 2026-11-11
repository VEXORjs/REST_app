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
    private final Map<UUID, ResourceAllocation> allocations = new ConcurrentHashMap<>();

    @Override
    public synchronized ResourceAllocation save(ResourceAllocation allocation) {
        if (allocation.getId() == null) {
            allocation.setId(UUID.randomUUID());
        }
        allocations.put(allocation.getId(), allocation);
        return allocation;
    }

    @Override
    public Optional<ResourceAllocation> findById(UUID id) {
        return Optional.ofNullable(allocations.get(id));
    }

    @Override
    public List<ResourceAllocation> findByClientId(UUID clientId) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findByResourceId(UUID resourceId) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findCurrentByClientId(UUID clientId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .filter(a -> a.getEndTime().after(now))
                .collect(Collectors.toList());
    }


    @Override
    public List<ResourceAllocation> findPastByClientId(UUID clientId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getClientId().equals(clientId))
                .filter(a -> !a.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findCurrentByResourceId(UUID resourceId, Date now) {
        return allocations.values().stream()
                .filter(a -> a.getResourceId().equals(resourceId))
                .filter(a -> a.getEndTime().after(now))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceAllocation> findPastByResourceId(UUID resourceId, Date now) {
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
    public synchronized void deleteById(UUID id) {
        allocations.remove(id);
    }
}
