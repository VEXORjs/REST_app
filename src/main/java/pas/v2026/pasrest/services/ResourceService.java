package pas.v2026.pasrest.services;

import org.springframework.stereotype.Service;
import pas.v2026.pasrest.dto.request.resource.UpdateResourceDto;
import pas.v2026.pasrest.exceptions.ResourceHasActiveAllocationsException;
import pas.v2026.pasrest.exceptions.ResourceNotFoundException;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.repositories.ResourceAllocationRepository;
import pas.v2026.pasrest.repositories.ResourceRepository;


import java.util.List;
import java.util.UUID;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final ResourceAllocationRepository allocationRepository;

    public ResourceService(ResourceRepository resourceRepository,
                           ResourceAllocationRepository allocationRepository) {
        this.resourceRepository = resourceRepository;
        this.allocationRepository = allocationRepository;
    }

    public synchronized Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    public Resource getResource(UUID id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    public List<Resource> getResourcesByNamePartial(String namePart) {
        return resourceRepository.findByNamePartial(namePart);
    }

    public List<Resource> getResourcesByAvailable(boolean available) {
        return resourceRepository.findByAvailable(available);
    }

    public synchronized void deleteResource(UUID id) {
        getResource(id);
        boolean hasAllocations = !allocationRepository.findByResourceId(id).isEmpty();
        if (hasAllocations) {
            throw new ResourceHasActiveAllocationsException(id);
        }
        resourceRepository.deleteById(id);
    }

    public synchronized Resource updateResource(UUID id, UpdateResourceDto dto) {
        Resource resource = getResource(id);
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setPrice(dto.getPrice());

        dto.updateSpecificFields(resource);
        return resource;
    }

}
