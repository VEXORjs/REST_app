package pas.v2026.pasrest.dto.response.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ClientResponseDto extends UserResponseDto {
}
