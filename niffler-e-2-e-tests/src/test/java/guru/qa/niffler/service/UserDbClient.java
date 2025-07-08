package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.User;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();

    public User createUser(User user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return xaTransaction(Connection.TRANSACTION_READ_COMMITTED,
                new Databases.XaFunction<>(connection -> {
                    return User.fromEntity(
                            new AuthUserDaoJdbc(connection).createUser(userEntity)
                    );
                }, CFG.authJdbcUrl()),
                new Databases.XaFunction<>(connection -> {
                    return User.fromEntity(
                            new UserdataUserDAOJdbc(connection).createUser(userEntity)
                    );
                },
                        CFG.userdataJdbcUrl()
                ));
    }
}
