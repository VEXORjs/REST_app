package pas.v2026.pasrest.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DataCenterResponseDto extends ResourceResponseDto {
    private String location;
    private double area;
    private int powerCapacity;
}
