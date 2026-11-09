package pas.v2026.pasrest.model;

import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Resource {

    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "Nazwa zasobu jest wymagana")
    private String name;
    private String description;
    private BigDecimal price;

    @Builder.Default
    private boolean available = true;

    private UUID ownerId;
}
