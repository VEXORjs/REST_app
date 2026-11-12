package pas.v2026.pasrest.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAllocation {
    @EqualsAndHashCode.Include
    private String id;

    @NotNull(message = "ID Klienta jest wymagane")
    private String clientId;

    @NotNull(message = "ID zasobu jest wymagane")
    private String resourceId;

    @NotNull(message = "Czas rozpoczęcia jest wymagany")
    private Date startTime;

    @NotNull(message = "Czas zakończenia jest wymagany")
    private Date endTime;

}
