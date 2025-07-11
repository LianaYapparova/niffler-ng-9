package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.user.UserEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Builder
@Data
public class AuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private UserAuthEntity user;
}
