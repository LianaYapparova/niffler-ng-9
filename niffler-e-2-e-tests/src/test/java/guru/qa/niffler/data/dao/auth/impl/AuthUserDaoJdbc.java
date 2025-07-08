package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.data.dao.auth.AuthUserDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.*;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {
    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }
    @Override
    public UserEntity createUser(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            ps.setString(1, user.getUsername());
            ps.setObject(2, encoder.encode(user.getPassword()));
            ps.setBoolean(3, true);
            ps.setBoolean(4, true);
            ps.setBoolean(5,true);
            ps.setBoolean(6, true);
            ps.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            new AuthAuthorityDaoJdbc(connection).createAuthority(AuthorityEntity.builder().user(user).authority(Authority.read).build());
            new AuthAuthorityDaoJdbc(connection).createAuthority(AuthorityEntity.builder().user(user).authority(Authority.write).build());
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
