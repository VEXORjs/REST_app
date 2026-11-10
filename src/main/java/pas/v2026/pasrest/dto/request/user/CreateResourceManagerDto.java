package pas.v2026.pasrest.dto.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.model.ResourceManager;
import pas.v2026.pasrest.model.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateResourceManagerDto extends CreateUserDto {
    @Override
    public User toEntity() {
        return ResourceManager.builder()
                .login(this.getLogin())
                .password(this.getPassword())
                .build();

    }
}
