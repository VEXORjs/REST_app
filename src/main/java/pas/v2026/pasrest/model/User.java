package pas.v2026.pasrest.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    @EqualsAndHashCode.Include
    private UUID id;

    @NotBlank(message = "Login nie może być pusty")
    private String login;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    @Builder.Default
    private boolean active = false;

    public abstract UserResponseDto toDto();
}
