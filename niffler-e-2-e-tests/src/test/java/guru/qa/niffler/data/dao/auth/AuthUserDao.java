package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    Optional<AuthUserEntity> findById(UUID id);

    AuthUserEntity createUser(AuthUserEntity user);

    List<AuthUserEntity> findAll();
}
