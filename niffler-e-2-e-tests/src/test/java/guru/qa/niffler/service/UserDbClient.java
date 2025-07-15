package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.util.Arrays;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public User createUserSpringJdbc(User user) {
        UserAuthEntity authUser = UserAuthEntity.builder()
                .username(user.username())
                .password(pe.encode("12345"))
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true).build();

        UserAuthEntity createdAuthUser = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .createUser(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = AuthorityEntity.builder().user(createdAuthUser).authority(e).build();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl()))
                .createAuthority(authorityEntities);

        return User.fromEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl()))
                        .createUser(
                                UserEntity.fromJson(user)
                        )
        );
    }

    public User createUser(User user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return xaTransaction(Connection.TRANSACTION_READ_COMMITTED, new Databases.XaFunction<>(connection -> {
                    UserAuthEntity userAuthEntity = UserAuthEntity.builder()
                            .username(userEntity.getUsername())
                            .password(pe.encode(userEntity.getPassword()))
                            .enabled(true)
                            .accountNonExpired(true)
                            .accountNonLocked(true)
                            .credentialsNonExpired(true)
                            .build();

                    userAuthEntity = new AuthUserDaoJdbc(connection).createUser(userAuthEntity);
                    new AuthAuthorityDaoJdbc(connection).createAuthority(AuthorityEntity.builder().user(userAuthEntity).authority(Authority.read).build());
                    new AuthAuthorityDaoJdbc(connection).createAuthority(AuthorityEntity.builder().user(userAuthEntity).authority(Authority.write).build());
                    return null;
                }, CFG.authJdbcUrl()
                ),
                new Databases.XaFunction<>(connection -> {
                    return User.fromEntity(
                            new UserdataUserDAOJdbc(connection).createUser(userEntity)
                    );
                }, CFG.userdataJdbcUrl()
                )
        );
    }
}
