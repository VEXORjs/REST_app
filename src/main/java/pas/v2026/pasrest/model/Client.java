package pas.v2026.pasrest.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.dto.response.user.ClientResponseDto;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Client extends User{

    @Override
    public UserResponseDto toDto() {
        return ClientResponseDto.builder()
                .id(this.getId())
                .login(this.getLogin())
                .active(this.isActive())
                .build();
    }

    @Override
    public String getUserRole() {
        return "CLIENT";
    }
}
