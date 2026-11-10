package pas.v2026.pasrest.dto.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.model.User;
import pas.v2026.pasrest.model.UserAdmin;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateAdminDto extends CreateUserDto {
    @Override
    public User toEntity() {
        return UserAdmin.builder()
                .login(this.getLogin())
                .password(this.getPassword())
                .build();
    }
}
