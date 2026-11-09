package pas.v2026.pasrest.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ClusterResponseDto extends ResourceResponseDto {
    private int numberOfServers;
    private String architecture;
}
