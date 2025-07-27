package guru.qa.niffler.data.repository.userdata;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserDataRepository {

    UserEntity create(UserEntity userEntity);

    Optional<UserEntity>  findById(UUID id);
    void addFriendshipRequest(UserEntity requester, UserEntity addressee);

    void addFriend(UserEntity requester, UserEntity addressee);

    Optional<UserEntity> findByUsername(String username);
}
