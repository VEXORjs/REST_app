package pas.v2026.pasrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.resource.ResourceResponseDto;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Server.class, name = "SERVER"),
        @JsonSubTypes.Type(value = Cluster.class, name = "CLUSTER"),
        @JsonSubTypes.Type(value = DataCenter.class, name = "DATA_CENTER")
})
public abstract class Resource {

    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @NotBlank(message = "Nazwa zasobu jest wymagana")
    private String name;
    private String description;
    private BigDecimal price;

    @Builder.Default
    private boolean available = true;

    private String ownerId;

    public abstract ResourceResponseDto toDto();
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public abstract Map<String, Object> getSpecificAttributes();
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public abstract String getResourceType();
}
