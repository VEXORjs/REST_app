package pas.v2026.pasrest.dto.request.resource;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pas.v2026.pasrest.model.Resource;

import java.math.BigDecimal;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateServerDto.class, name = "SERVER"),
        @JsonSubTypes.Type(value = UpdateClusterDto.class, name = "CLUSTER"),
        @JsonSubTypes.Type(value = UpdateDataCenterDto.class, name = "DATA_CENTER")
})
public abstract class UpdateResourceDto {

    @NotBlank(message = "Nazwa nie może być pusta")
    private String name;

    @NotBlank(message = "Opis nie może być pusty")
    private String description;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena musi być większa niż zero")
    private BigDecimal price;

    public abstract void updateSpecificFields(Resource resource);
}
