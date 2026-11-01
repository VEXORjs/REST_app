package repository;

import model.Client;
import model.Resource;
import model.ResourceAllocation;
import model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ResourceAllocationRepository {
    private List<ResourceAllocation> resourceAllocations = new ArrayList<>();

    private ResourceRepository resourceRepository;
    private UserRepository userRepository;

    public ResourceAllocationRepository(ResourceRepository resourceRepository, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public ResourceAllocation createResourceAllocation(
            UUID clientId, UUID resourceId,
            Date startTime, Date endTime
    ){
        User user = userRepository.findById(clientId);

        if (!(user instanceof Client)) {
            throw new IllegalArgumentException("Nie znaleziono Usera");
        }

        Client client = (Client) user;

        if (!client.isActive()) {
            throw new IllegalArgumentException("Klient nie jest aktywny");
        }

        Resource resource = resourceRepository.findById(resourceId);

        if (!(resource instanceof Resource)) {
            throw new IllegalArgumentException("Nie znaleziono Resource");
        }

        if (startTime == null || endTime == null || !endTime.after(startTime)) {
            throw new IllegalArgumentException("Nieprawidlowy przedzial czasu");
        }

        for (ResourceAllocation resourceAllocation : resourceAllocations) {
            if (resourceAllocation.getResource().equals(resource) && resourceAllocation.getEndTime().after(new Date())) {
                throw  new IllegalArgumentException("Resource jest alokowany przez kogos");
            }
        }

        ResourceAllocation resourceAllocation = new ResourceAllocation();

        resourceAllocation.setClient(client);
        resourceAllocation.setResource(resource);
        resourceAllocation.setStartTime(startTime);
        resourceAllocation.setEndTime(endTime);

        resourceAllocations.add(resourceAllocation);

        return resourceAllocation;
    }

    public List<ResourceAllocation> getResourceAllocations() {
        return new ArrayList<>(resourceAllocations);
    }

}
