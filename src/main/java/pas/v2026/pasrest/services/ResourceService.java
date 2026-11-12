package pas.v2026.pasrest.services;

import org.springframework.stereotype.Service;
import pas.v2026.pasrest.dto.request.resource.UpdateResourceDto;
import pas.v2026.pasrest.exceptions.ResourceHasActiveAllocationsException;
import pas.v2026.pasrest.exceptions.ResourceNotFoundException;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.repositories.ResourceAllocationRepository;
import pas.v2026.pasrest.repositories.ResourceRepository;


import java.util.Date;
import java.util.List;

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

    public Resource getResource(String id) {
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

    public synchronized void deleteResource(String id) {
        getResource(id);
        boolean hasAllocations = !allocationRepository.findCurrentByResourceId(id, new Date()).isEmpty();
        if (hasAllocations) {
            throw new ResourceHasActiveAllocationsException(id);
        }
        resourceRepository.deleteById(id);
    }

    public synchronized Resource updateResource(String id, UpdateResourceDto dto) {
        Resource resource = getResource(id);
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setPrice(dto.getPrice());

        dto.updateSpecificFields(resource);
        return resourceRepository.save(resource);
    }

}
