package pas.v2026.pasrest.model;

import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pas.v2026.pasrest.dto.response.user.AdminResponseDto;
import pas.v2026.pasrest.dto.response.user.UserResponseDto;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserAdmin extends User{

    @Override
    public UserResponseDto toDto() {
        return AdminResponseDto.builder()
                .id(this.getId())
                .login(this.getLogin())
                .active(this.isActive())
                .build();

    }

    @Override
    public String getUserRole() {
        return "ADMIN";
    }
}
