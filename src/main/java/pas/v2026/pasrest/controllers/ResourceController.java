package pas.v2026.pasrest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pas.v2026.pasrest.dto.request.resource.*;
import pas.v2026.pasrest.dto.response.resource.ResourceResponseDto;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.services.ResourceService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<ResourceResponseDto> createResource(@Valid @RequestBody CreateResourceDto dto) {
        Resource resource = mapToEntity(dto);
        Resource savedResource = resourceService.saveResource(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(savedResource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDto> getResource(@PathVariable UUID id) {
        Resource resource = resourceService.getResource(id);
        return ResponseEntity.ok(mapToDto(resource));
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponseDto>> getResources(
            @RequestParam(required = false) String partialName,
            @RequestParam(required = false) Boolean available) {
        List<Resource> resources;

        if (partialName != null) {
            resources = resourceService.getResourcesByNamePartial(partialName);
        } else if (available != null) {
            resources = resourceService.getResourcesByAvailable(available);
        } else {
            resources = resourceService.getAllResources();
        }

        List<ResourceResponseDto> response = resources.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable UUID id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDto> updateResource(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateResourceDto dto) {
        Resource updatedResource = resourceService.updateResource(id, dto);
        return ResponseEntity.ok(mapToDto(updatedResource));
    }

    private Resource mapToEntity(CreateResourceDto dto) {
        return dto.toEntity();
    }

    private ResourceResponseDto mapToDto(Resource resource) {
        return resource.toDto();
    }
}
