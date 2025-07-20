package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.auth.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDAOJdbc;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.auth.AuthUserRepository;
import guru.qa.niffler.data.repository.auth.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.userdata.UserDataRepository;
import guru.qa.niffler.data.repository.userdata.impl.UserDataRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDaoJdbc authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDaoJdbc authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDAOJdbc userUserDao = new UserdataUserDAOJdbc();

    private final AuthAuthorityDaoSpringJdbc authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();
    private final AuthUserDaoSpringJdbc authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();
    private final UserdataUserDaoSpringJdbc userdataUserDaoSpringJdbc = new UserdataUserDaoSpringJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();

    private final UserDataRepository userDataRepository = new UserDataRepositoryJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    private final JdbcTransactionTemplate jdbcTransactionTemplate = new JdbcTransactionTemplate(CFG.userdataJdbcUrl());

    public UserJson createUserSpringJdbc(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toList());

            authUserRepository.create(authUser);
            return UserJson.fromEntity(
                    userDataRepository.create(UserEntity.fromJson(user))
            );
        });
    }

    public UserJson createUser(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    authUser = authUserDao.createUser(authUser);
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setUser(authUser);
                    authorityEntity.setAuthority(Authority.write);

                    authAuthorityDao.createAuthority(authorityEntity);
                    authorityEntity.setAuthority(Authority.read);
                    authAuthorityDao.createAuthority(authorityEntity);
                    return null;
                },
                () -> {
                    return UserJson.fromEntity(
                            userUserDao.createUser(userEntity)
                    );
                });
    }
}
