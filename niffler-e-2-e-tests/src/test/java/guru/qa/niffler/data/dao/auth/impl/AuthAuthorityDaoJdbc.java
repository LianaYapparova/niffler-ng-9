package guru.qa.niffler.data.dao.auth.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.auth.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.*;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {
    private static final Config CFG = Config.getInstance();
    @Override
    public AuthorityEntity createAuthority(AuthorityEntity authorityEntity) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES ( ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authorityEntity.getUser().getId());
            ps.setString(2, authorityEntity.getAuthority().toString());
            ps.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authorityEntity.setId(generatedKey);
            return authorityEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthorityEntity createAuthority(AuthorityEntity... authority) {
        return null;
    }

    @Override
    public List<AuthorityEntity> findAll() {
        return null;
    }
}
