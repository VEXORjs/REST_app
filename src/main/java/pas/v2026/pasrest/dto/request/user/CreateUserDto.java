package pas.v2026.pasrest.dto.request.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pas.v2026.pasrest.model.User;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateClientDto.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = CreateAdminDto.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = CreateResourceManagerDto.class, name = "RESOURCE_MANAGER")
})
public abstract class CreateUserDto {
    @NotBlank(message = "Login nie może być pusty")
    @Size(min = 3, max = 50, message = "Login musi mieć od 3 do 50 znaków")
    private String login;

    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 6, message = "Hasło musi mieć minimum 6 znaków")
    private String password;

    public abstract User toEntity();
}
