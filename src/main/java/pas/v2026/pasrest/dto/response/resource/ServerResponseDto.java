package pas.v2026.pasrest.dto.response.resource;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ServerResponseDto extends ResourceResponseDto {
    private String manufacturer;
    private String cpuName;
    private int numberOfCpus;
    private int ram;
    private int storageCapacity;
}
