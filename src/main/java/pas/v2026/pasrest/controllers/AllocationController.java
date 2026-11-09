package pas.v2026.pasrest.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pas.v2026.pasrest.dto.request.CreateAllocationDto;
import pas.v2026.pasrest.dto.response.AllocationResponseDto;
import pas.v2026.pasrest.model.ResourceAllocation;
import pas.v2026.pasrest.services.ResourceAllocationService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {
    private final ResourceAllocationService allocationService;

    public AllocationController(ResourceAllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping
    public ResponseEntity<AllocationResponseDto> createAllocation(@Valid @RequestBody CreateAllocationDto dto) {
        ResourceAllocation allocation = allocationService.createAllocation(
                dto.getClientId(),
                dto.getResourceId(),
                dto.getStartTime(),
                dto.getEndTime()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(allocation));
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<AllocationResponseDto> finishAllocation(@PathVariable UUID id) {
        ResourceAllocation allocation = allocationService.finishAllocation(id);
        return ResponseEntity.ok(mapToDto(allocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllocation(@PathVariable UUID id) {
        allocationService.deleteUnfinishedAllocation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AllocationResponseDto>> getClientAllocations(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "true") boolean current) {
        List<ResourceAllocation> allocations = current
        ? allocationService.getCurrentAllocationsForClient(clientId)
        : allocationService.getPastAllocationsForClient(clientId);

        List<AllocationResponseDto> response = allocations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<AllocationResponseDto>> getResourceAllocations(
            @PathVariable UUID resourceId,
            @RequestParam(defaultValue = "true") boolean current) {
        List<ResourceAllocation> allocations = current
                ? allocationService.getCurrentAllocationsForResource(resourceId)
                : allocationService.getPastAllocationsForResource(resourceId);

        List<AllocationResponseDto> response = allocations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private AllocationResponseDto mapToDto(ResourceAllocation allocation) {
        return AllocationResponseDto.builder()
                .id(allocation.getId())
                .clientId(allocation.getClientId())
                .resourceId(allocation.getResourceId())
                .startTime(allocation.getStartTime())
                .endTime(allocation.getEndTime())
                .build();
    }

}
