package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UserDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UsersClient {
  static UsersClient getInstance() {
    return new UserDbClient();
  }

  @Nonnull
  UserJson createUser(UserJson userJson);

  @Nonnull
  List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

  @Nonnull
  List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

  @Nonnull
  List<UserJson> addFriend(UserJson targetUser, int count);
  @Nonnull
  UserJson  createUserSpringJdbc(UserJson userJson);
  @Nonnull
  UserJson  createUserHibernate(UserJson userJson);
}
