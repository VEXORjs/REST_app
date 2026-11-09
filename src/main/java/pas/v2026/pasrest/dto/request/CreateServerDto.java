package pas.v2026.pasrest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateServerDto extends CreateResourceDto {

    @NotBlank(message = "Producent jest wymagany")
    private String manufacturer;

    @NotBlank(message = "Nazwa procesora jest wymagana")
    private String nameOfCpu;

    @Min(value = 1, message = "Ilość rdzeni procesora musi być większa od zera")
    private int numberOfCpus;

    @Min(value = 1, message = "Ilość RAMu musi być większy od zera")
    private int ram;

    @Min(value = 1, message = "Pojemność pamięci dyskowej musi być większa od zera")
    private int storageCapacity;
}
