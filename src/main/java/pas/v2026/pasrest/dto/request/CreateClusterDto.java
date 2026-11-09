package pas.v2026.pasrest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateClusterDto extends CreateResourceDto {

    @NotNull(message = "Liczba serwerów jest wymagana")
    @Min(value = 1, message = "Liczba serwerów musi być większa od zera")
    private int numberOfServers;

    @NotBlank(message = "Nazwa architektury jest wymagana")
    private String architecture;

}
