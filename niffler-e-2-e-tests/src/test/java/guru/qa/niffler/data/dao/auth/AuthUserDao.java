package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.UserAuthEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    Optional<UserAuthEntity> findById(UUID id);

    UserAuthEntity createUser(UserAuthEntity user);
}
