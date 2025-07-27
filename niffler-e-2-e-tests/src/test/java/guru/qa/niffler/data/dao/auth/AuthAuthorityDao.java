package guru.qa.niffler.data.dao.auth;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.List;
import java.util.UUID;

public interface AuthAuthorityDao {

    AuthorityEntity createAuthority(AuthorityEntity user);

     AuthorityEntity createAuthority(AuthorityEntity... authority);

     List<AuthorityEntity> findAll();

    List<AuthorityEntity> findAllByUserId(UUID userId);
}
