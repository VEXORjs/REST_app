package pas.v2026.pasrest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = UserAdmin.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = ResourceManager.class, name = "MANAGER")
})
public abstract class User {

    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @NotBlank(message = "Login nie może być pusty")
    private String login;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;

    @Builder.Default
    private boolean active = false;

    public abstract UserResponseDto toDto();
    public abstract String getUserRole();
}
