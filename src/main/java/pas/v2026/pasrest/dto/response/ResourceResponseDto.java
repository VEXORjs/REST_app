package pas.v2026.pasrest.dto.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerResponseDto.class, name = "SERVER"),
        @JsonSubTypes.Type(value = ClusterResponseDto.class, name = "CLUSTER"),
        @JsonSubTypes.Type(value = DataCenterResponseDto.class, name = "DATACENTER")
})
public abstract class ResourceResponseDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available = true;
    private UUID ownerID;
}
