package pas.v2026.pasrest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class AllocationResponseDto {
    private String id;
    private String clientId;
    private String resourceId;
    private Date startTime;
    private Date endTime;
}
