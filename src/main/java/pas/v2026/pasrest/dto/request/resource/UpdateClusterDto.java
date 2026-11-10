package pas.v2026.pasrest.dto.request.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.exceptions.UnknownResourceTypeException;
import pas.v2026.pasrest.model.Cluster;
import pas.v2026.pasrest.model.Resource;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateClusterDto extends UpdateResourceDto{
    @NotNull(message = "Liczba serwerów jest wymagana")
    @Min(value = 1, message = "Liczba serwerów musi być większa od zera")
    private int numberOfServers;

    @NotBlank(message = "Nazwa architektury jest wymagana")
    private String architecture;

    @Override
    public void updateSpecificFields(Resource resource) {
        if (!(resource instanceof Cluster c)) {
            throw new UnknownResourceTypeException("Niezgodność typów: Oczekiwano Cluster.");
        }

        c.setNumberOfServers(this.numberOfServers);
        c.setArchitecture(this.architecture);
    }
}
