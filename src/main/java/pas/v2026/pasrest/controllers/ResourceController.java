package pas.v2026.pasrest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pas.v2026.pasrest.dto.request.*;
import pas.v2026.pasrest.dto.response.ClusterResponseDto;
import pas.v2026.pasrest.dto.response.DataCenterResponseDto;
import pas.v2026.pasrest.dto.response.ResourceResponseDto;
import pas.v2026.pasrest.dto.response.ServerResponseDto;
import pas.v2026.pasrest.exceptions.UnknownResourceTypeException;
import pas.v2026.pasrest.model.Cluster;
import pas.v2026.pasrest.model.DataCenter;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.model.Server;
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
        if (dto instanceof CreateDataCenterDto dcDto) {
            return DataCenter.builder()
                    .name(dcDto.getName())
                    .description(dcDto.getDescription())
                    .price(dcDto.getPrice())
                    .location(dcDto.getLocation())
                    .area(dcDto.getArea())
                    .powerCapacity(dcDto.getPowerCapacity())
                    .build();
        } else if (dto instanceof CreateClusterDto clusterDto) {
            return Cluster.builder()
                    .name(clusterDto.getName())
                    .description(clusterDto.getDescription())
                    .price(clusterDto.getPrice())
                    .numberOfServers(clusterDto.getNumberOfServers())
                    .architecture(clusterDto.getArchitecture())
                    .build();
        } else if (dto instanceof CreateServerDto serverDto) {
            return Server.builder()
                    .name(serverDto.getName())
                    .description(serverDto.getDescription())
                    .price(serverDto.getPrice())
                    .manufacturer(serverDto.getManufacturer())
                    .cpuName(serverDto.getNameOfCpu())
                    .ram(serverDto.getRam())
                    .storageCapacity(serverDto.getStorageCapacity())
                    .build();
        }
        throw new UnknownResourceTypeException("Nieznany typ zasobu w żądaniu");
    }

    private ResourceResponseDto mapToDto(Resource resource) {
        if (resource instanceof DataCenter dc) {
            return DataCenterResponseDto.builder()
                    .id(dc.getId())
                    .name(dc.getName())
                    .description(dc.getDescription())
                    .price(dc.getPrice())
                    .available(dc.isAvailable())
                    .ownerId(dc.getOwnerId())
                    .location(dc.getLocation())
                    .area(dc.getArea())
                    .powerCapacity(dc.getPowerCapacity())
                    .build();
        } else if (resource instanceof Cluster c) {
            return ClusterResponseDto.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .price(c.getPrice())
                    .available(c.isAvailable())
                    .ownerId(c.getOwnerId())
                    .numberOfServers(c.getNumberOfServers())
                    .architecture(c.getArchitecture())
                    .build();
        } else if (resource instanceof Server s) {
            return ServerResponseDto.builder()
                    .id(s.getId())
                    .name(s.getName())
                    .description(s.getDescription())
                    .price(s.getPrice())
                    .available(s.isAvailable())
                    .ownerId(s.getOwnerId())
                    .manufacturer(s.getManufacturer())
                    .cpuName(s.getCpuName())
                    .numberOfCpus(s.getNumberOfCpus())
                    .ram(s.getRam())
                    .storageCapacity(s.getStorageCapacity())
                    .build();
        }
        throw new UnknownResourceTypeException("Nieznany typ zasobu w bazie");
    }
}
