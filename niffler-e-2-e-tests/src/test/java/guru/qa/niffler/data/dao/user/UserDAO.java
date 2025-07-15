package guru.qa.niffler.data.dao.user;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDAO {
    UserEntity createUser(UserEntity user);
    Optional<UserEntity> findUserById(UUID id);
    Optional<UserEntity> findUserByUsername(String username);
    void deleteUser(UserEntity user);
    List<UserEntity> findAll();
}
