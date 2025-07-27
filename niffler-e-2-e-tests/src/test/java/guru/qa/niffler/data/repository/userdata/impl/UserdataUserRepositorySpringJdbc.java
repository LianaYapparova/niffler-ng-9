package guru.qa.niffler.data.repository.userdata.impl;


import guru.qa.niffler.data.dao.user.UserDAO;
import guru.qa.niffler.data.dao.user.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.repository.userdata.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserDataRepository {

  private final UserDAO udUserDao = new UserdataUserDaoSpringJdbc();

  @Override
  public UserEntity create(UserEntity user) {
    return udUserDao.createUser(user);
  }

  @Override
  public Optional<UserEntity> findById(UUID id) {
    return udUserDao.findUserById(id);
  }

  @Override
  public Optional<UserEntity> findByUsername(String username) {
    return udUserDao.findUserByUsername(username);
  }

  @Override
  public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.PENDING, addressee);
    udUserDao.update(requester);
  }


  @Override
  public void addFriend(UserEntity requester, UserEntity addressee) {
    requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
    addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
    udUserDao.update(requester);
    udUserDao.update(addressee);
  }
}