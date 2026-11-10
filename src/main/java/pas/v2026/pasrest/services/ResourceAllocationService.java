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
import java.util.UUID;

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

    public synchronized ResourceAllocation createAllocation(UUID clientId, UUID resourceId, Date startTime, Date endTime) {

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

        List<ResourceAllocation> existingAllocations = allocationRepository.findByResourceId(resourceId);

        boolean isOverlapping = existingAllocations.stream().anyMatch(alloc ->
                isTimeOverlapping(startTime, endTime, alloc.getStartTime(), alloc.getEndTime())
        );

        if (isOverlapping) {
            throw new ResourceAlreadyAllocatedException(resourceId);
        }

        ResourceAllocation newAllocation = ResourceAllocation.builder()
                .clientId(client.getId())
                .resourceId(resource.getId())
                .startTime(startTime)
                .endTime(endTime)
                .build();

        return allocationRepository.save(newAllocation);
    }

    public synchronized ResourceAllocation finishAllocation(UUID allocationId) {
        ResourceAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException(allocationId));

        Date now = new Date();

        if (allocation.getEndTime().before(now)) {
            throw new AllocationAlreadyFinishedException();
        }

        allocation.setEndTime(now);
        return allocationRepository.save(allocation);
    }

    public synchronized void deleteUnfinishedAllocation(UUID allocationId) {
        ResourceAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new AllocationNotFoundException(allocationId));

        Date now = new Date();

        if (allocation.getEndTime().before(now)) {
            throw new AllocationAlreadyFinishedException();
        }

        allocationRepository.deleteById(allocationId);
    }

    public List<ResourceAllocation> getCurrentAllocationsForClient(UUID clientId) {
        return allocationRepository.findCurrentByClientId(clientId, new Date());
    }

    public List<ResourceAllocation> getPastAllocationsForClient(UUID clientId) {
        return allocationRepository.findPastByClientId(clientId, new Date());
    }

    public List<ResourceAllocation> getCurrentAllocationsForResource(UUID resourceId) {
        return allocationRepository.findCurrentByResourceId(resourceId, new Date());
    }

    public List<ResourceAllocation> getPastAllocationsForResource(UUID resourceId) {
        return allocationRepository.findPastByResourceId(resourceId, new Date());
    }

    public List<ResourceAllocation> getAllAllocations() {
        return allocationRepository.findAll();
    }

    public List<ResourceAllocation> getAllAllocationsForClient(UUID clientId) {
        return allocationRepository.findByClientId(clientId);
    }

    private boolean isTimeOverlapping(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && start2.before(end1);
    }

}
