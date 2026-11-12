package pas.v2026.pasrest.services;

import org.springframework.stereotype.Service;
import pas.v2026.pasrest.exceptions.*;
import pas.v2026.pasrest.model.Client;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.model.ResourceAllocation;
import pas.v2026.pasrest.model.User;
import pas.v2026.pasrest.repositories.ResourceAllocationRepository;
import pas.v2026.pasrest.repositories.ResourceRepository;
import pas.v2026.pasrest.repositories.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceAllocationService {

    private final ResourceAllocationRepository allocationRepository;
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;

    public ResourceAllocationService(ResourceAllocationRepository allocationRepository,
                                     UserRepository userRepository,
                                     ResourceRepository resourceRepository) {
        this.allocationRepository = allocationRepository;
        this.userRepository = userRepository;
        this.resourceRepository = resourceRepository;
    }

    public synchronized ResourceAllocation createAllocation(String clientId, String resourceId, Date startTime, Date endTime) {

        if (!endTime.after(startTime)) {
            throw new InvalidDateRangeException();
        }

        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new UserNotFoundException(clientId));

        if (!(user instanceof Client client)) {
            throw new InvalidUserRoleException();
        }

        if (!client.isActive()) {
            throw new InActiveUserException(clientId);
        }

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException(resourceId));

        if (!resource.isAvailable()) {
            throw new ResourceUnavailableException();
        }

        boolean isOverlapping = allocationRepository.hasOverlappingAllocations(resourceId, startTime, endTime);


        if (isOverlapping) {
            throw new ResourceAlreadyAllocatedException(resourceId);
        }

        ResourceAllocation newAllocation = ResourceAllocation.builder()
                .clientId(client.getId())
                .resourceId(resource.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        ResourceAllocation savedAllocation = allocationRepository.save(newAllocation);
        updateResourceState(resource.getId());
        return savedAllocation;
    }

    public synchronized ResourceAllocation finishAllocation(String allocationId) {
        ResourceAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException(allocationId));

        Date now = new Date();

        if (allocation.getEndTime().before(now)) {
            throw new AllocationAlreadyFinishedException();
        }

        if (allocation.getStartTime().after(now)) {
            throw new InvalidDateRangeException("Nie można zakończyć alokacji, która się jeszcze nie rozpoczęła. Zamiast tego anuluj ją (usuń)");
        }

        allocation.setEndTime(now);
        ResourceAllocation savedAllocation = allocationRepository.save(allocation);
        updateResourceState(allocation.getResourceId());
        return savedAllocation;
    }

    public synchronized void deleteUnfinishedAllocation(String allocationId) {
        ResourceAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException(allocationId));

        Date now = new Date();

        if (allocation.getEndTime().before(now)) {
            throw new AllocationAlreadyFinishedException();
        }

        allocationRepository.deleteById(allocationId);
        updateResourceState(allocation.getResourceId());
    }

    public List<ResourceAllocation> getCurrentAllocationsForClient(String clientId) {
        return allocationRepository.findCurrentByClientId(clientId, new Date());
    }

    public List<ResourceAllocation> getPastAllocationsForClient(String clientId) {
        return allocationRepository.findPastByClientId(clientId, new Date());
    }

    public List<ResourceAllocation> getCurrentAllocationsForResource(String resourceId) {
        return allocationRepository.findCurrentByResourceId(resourceId, new Date());
    }

    public List<ResourceAllocation> getPastAllocationsForResource(String resourceId) {
        return allocationRepository.findPastByResourceId(resourceId, new Date());
    }

    public List<ResourceAllocation> getAllAllocations() {
        return allocationRepository.findAll();
    }

    public List<ResourceAllocation> getAllAllocationsForClient(String clientId) {
        return allocationRepository.findByClientId(clientId);
    }

    private void updateResourceState(String resourceId) {
        Resource resource = resourceRepository.findById(resourceId).orElse(null);
        if (resource == null) return;

        Date now = new Date();

        Optional<ResourceAllocation> currentOrFutureAllocation = allocationRepository.findByResourceId(resourceId).stream()
                .filter(a -> a.getEndTime().after(now))
                .findFirst();

        if (currentOrFutureAllocation.isPresent()) {
            resource.setAvailable(false);
            resource.setOwnerId(currentOrFutureAllocation.get().getClientId());
        } else {
            resource.setAvailable(true);
            resource.setOwnerId(null);
        }

        resourceRepository.save(resource);
    }

}
