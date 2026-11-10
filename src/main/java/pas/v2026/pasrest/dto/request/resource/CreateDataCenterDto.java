package pas.v2026.pasrest.dto.request.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.model.DataCenter;
import pas.v2026.pasrest.model.Resource;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateDataCenterDto extends CreateResourceDto {

    @NotBlank(message = "Lokalizacja jest wymagana")
    private String location;

    @NotNull(message = "Powierzchnia jest wymagana")
    @Positive(message = "Powierzchnia (area) musi być większa od zera")
    private double area;

    @NotNull(message = "Moc jest wymagana")
    @Min(value = 1, message = "Moc (power capacity) musi wynosić co najmniej 1")
    private int powerCapacity;

    @Override
    public Resource toEntity() {
        return DataCenter.builder()
                .name(this.getName())
                .description(this.getDescription())
                .price(this.getPrice())
                .location(this.getLocation())
                .area(this.getArea())
                .powerCapacity(this.getPowerCapacity())
                .build();
    }
}
