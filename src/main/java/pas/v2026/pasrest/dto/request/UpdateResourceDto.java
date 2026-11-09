package pas.v2026.pasrest.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateServerDto.class, name = "SERVER"),
        @JsonSubTypes.Type(value = CreateClusterDto.class, name = "CLUSTER"),
        @JsonSubTypes.Type(value = CreateDataCenterDto.class, name = "DATACENTER")
})
public abstract class UpdateResourceDto {

    @NotBlank(message = "Nazwa nie może być pusta")
    private String name;

    @NotBlank(message = "Opis nie może być pusty")
    private String description;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cena musi być większa niż zero")
    private BigDecimal price;
}
