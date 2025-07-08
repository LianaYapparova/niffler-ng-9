package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.user.UserEntity;

public interface AuthUserDao {
    UserEntity createUser(UserEntity user);
}
