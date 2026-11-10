package pas.v2026.pasrest.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotBlank(message = "Nowe hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;
}
