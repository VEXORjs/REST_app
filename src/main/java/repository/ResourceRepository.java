package repository;

import model.Resource;
import model.ResourceAllocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourceRepository {

    private List<Resource> resources = new ArrayList<>();
    private List<ResourceAllocation> allocations;

    public ResourceRepository(List<ResourceAllocation> allocations) {
        this.allocations = allocations;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public List<Resource> getAllResources() {
        return new ArrayList<>(resources);
    }

    public boolean updateResource(Resource updatedResource) {
        for  (Resource resource : resources) {
            if(resource.getId().equals(updatedResource.getId())) {
                resource.setName(updatedResource.getName());
                resource.setDescription(updatedResource.getDescription());
                resource.setPrice(updatedResource.getPrice());
                resource.setAvailable(updatedResource.isAvailable());

                return true;
            }
        }
        return false;
    }

    public boolean deleteResource(UUID id) {
        Resource resourceToDelete = null;
        for(Resource resource : resources) {
            if(resource.getId().equals(id)) {
                resourceToDelete = resource;
                break;
            }
        }

        if (resourceToDelete ==  null) {
            return false;
        }

        for (ResourceAllocation allocation : allocations) {
            if(allocation.getResource().getId().equals(resourceToDelete.getId())) {
                return false;
            }
        }
        resources.remove(resourceToDelete);
        return true;
    }

    public Resource findById(UUID id) {
        for (Resource resource : resources) {
            if (resource.getId().equals(id)) {
                return resource;
            }
        }
        return null;
    }
}