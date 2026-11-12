package pas.v2026.pasrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.resource.DataCenterResponseDto;
import pas.v2026.pasrest.dto.response.resource.ResourceResponseDto;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DataCenter extends Resource{
    private String location;
    private double area;
    private int powerCapacity;

    @Override
    public ResourceResponseDto toDto() {
        return DataCenterResponseDto.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .price(this.getPrice())
                .available(this.isAvailable())
                .ownerId(this.getOwnerId())
                .location(this.getLocation())
                .area(this.getArea())
                .powerCapacity(this.getPowerCapacity())
                .build();
    }

    @Override
    public Map<String, Object> getSpecificAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("location", getLocation());
        attributes.put("area", getArea());
        attributes.put("powerCapacity", getPowerCapacity());
        return attributes;
    }

    @Override
    public String getResourceType() {
        return "DATACENTER";
    }
}
