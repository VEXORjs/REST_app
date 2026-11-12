package pas.v2026.pasrest.dto.response.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "role")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientResponseDto.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = AdminResponseDto.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = ResourceManagerResponseDto.class, name = "RESOURCE_MANAGER")
})
public class UserResponseDto {
    private String id;
    private String login;
    private boolean active;
}
