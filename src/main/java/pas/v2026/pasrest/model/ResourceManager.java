package pas.v2026.pasrest.model;

import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pas.v2026.pasrest.dto.response.user.ResourceManagerResponseDto;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ResourceManager extends User {
    @Override
    public UserResponseDto toDto() {
        return ResourceManagerResponseDto.builder()
                .id(this.getId())
                .login(this.getLogin())
                .active(this.isActive()).build();

    }
}
