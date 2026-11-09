package pas.v2026.pasrest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class AllocationResponseDto {
    private UUID id;
    private UUID clientId;
    private UUID resourceId;
    private Date startTime;
    private Date endTime;
}
