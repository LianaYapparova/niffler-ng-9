package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthUserEntityRowMapper implements RowMapper<UserAuthEntity> {

    public static final AuthUserEntityRowMapper instance = new AuthUserEntityRowMapper();

    private AuthUserEntityRowMapper() {
    }

    @Override
    public UserAuthEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAuthEntity result = UserAuthEntity.builder()
                .id(rs.getObject("id", UUID.class))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .enabled(rs.getBoolean("enabled"))
                .accountNonExpired(rs.getBoolean("account_non_expired"))
                .accountNonLocked(rs.getBoolean("account_non_locked"))
                .credentialsNonExpired(rs.getBoolean("credentials_non_expired"))
                .build();
        return result;
    }
}
