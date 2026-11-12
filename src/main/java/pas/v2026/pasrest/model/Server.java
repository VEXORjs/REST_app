package pas.v2026.pasrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.resource.ResourceResponseDto;
import pas.v2026.pasrest.dto.response.resource.ServerResponseDto;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Server extends Resource{
    private String manufacturer;
    private String cpuName;
    private int numberOfCpus;
    private int ram;
    private int storageCapacity;

    @Override
    public ResourceResponseDto toDto() {
        return ServerResponseDto.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .price(this.getPrice())
                .available(this.isAvailable())
                .ownerId(this.getOwnerId())
                .manufacturer(this.getManufacturer())
                .cpuName(this.getCpuName())
                .numberOfCpus(this.getNumberOfCpus())
                .ram(this.getRam())
                .storageCapacity(this.getStorageCapacity())
                .build();
    }

    @Override
    public Map<String, Object> getSpecificAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("manufacturer", getManufacturer());
        attributes.put("cpuName", getCpuName());
        attributes.put("numberOfCpus", getNumberOfCpus());
        attributes.put("ram", getRam());
        attributes.put("storageCapacity", getStorageCapacity());
        return attributes;
    }

    @Override
    public String getResourceType() {
        return "SERVER";
    }
}
