package pas.v2026.pasrest.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CreateAllocationDto {

    @NotNull(message = "ID klienta jest wymagane")
    private String clientId;

    @NotNull(message = "ID zasobu jest wymagane")
    private String resourceId;

    @NotNull(message = "Czas rozpoczęcia jest wymagany")
    @FutureOrPresent(message = "Czas rozpoczęcia nie może być w przeszłości")
    private Date startTime;

    @NotNull(message =  "Czas zakończenia jest wymagany")
    @Future(message = "Czas zakończenia musi być w przyszłości")
    private Date endTime;
}
