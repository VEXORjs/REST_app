package pas.v2026.pasrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.resource.ClusterResponseDto;
import pas.v2026.pasrest.dto.response.resource.ResourceResponseDto;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Cluster extends Resource{
    private int numberOfServers;
    private String architecture;

    @Override
    public ResourceResponseDto toDto() {
        return ClusterResponseDto.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .price(this.getPrice())
                .available(this.isAvailable())
                .ownerId(this.getOwnerId())
                .numberOfServers(this.getNumberOfServers())
                .architecture(this.getArchitecture())
                .build();
    }

    @Override
    public Map<String, Object> getSpecificAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("numberOfServers", getNumberOfServers());
        attributes.put("architecture", getArchitecture());
        return attributes;
    }

    @Override
    public String getResourceType() {
        return "CLUSTER";
    }
}
