package pas.v2026.pasrest.dto.request.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pas.v2026.pasrest.model.Client;
import pas.v2026.pasrest.model.User;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateClientDto extends CreateUserDto {

    @Override
    public User toEntity() {
        return Client.builder()
                .login(this.getLogin())
                .password(this.getPassword())
                .build();
    }
}
