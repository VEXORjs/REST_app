package pas.v2026.pasrest.dto.request.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.model.Resource;
import pas.v2026.pasrest.model.Server;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateServerDto extends CreateResourceDto {

    @NotBlank(message = "Producent jest wymagany")
    private String manufacturer;

    @NotBlank(message = "Nazwa procesora jest wymagana")
    private String nameOfCpu;

    @NotNull(message = "Ilość rdzeni jest wymagana")
    @Min(value = 1, message = "Ilość rdzeni procesora musi być większa od zera")
    private int numberOfCpus;

    @NotNull(message = "Ilość RAMu jest wymagana")
    @Min(value = 1, message = "Ilość RAMu musi być większy od zera")
    private int ram;

    @NotNull(message = "Pojemność pamięci dyskowej jest wymagana")
    @Min(value = 1, message = "Pojemność pamięci dyskowej musi być większa od zera")
    private int storageCapacity;

    @Override
    public Resource toEntity() {
        return Server.builder()
                .name(this.getName())
                .description(this.getDescription())
                .price(this.getPrice())
                .manufacturer(this.getManufacturer())
                .cpuName(this.getNameOfCpu())
                .ram(this.getRam())
                .storageCapacity(this.getStorageCapacity())
                .build();
    }
}
