package guru.qa.niffler.test;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.jupiter.extension.UserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.GatewayApiClient;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTest
public class FriendsRestTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLogin = ApiLoginExtension.rest();

  private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
  private final GatewayV2ApiClient gatewayApiV2Client = new GatewayV2ApiClient();
  private final AuthApiClient authApiClient = new AuthApiClient();


  @User(
          friends = 1,
          incomeInvitations = 2
  )
  @ApiLogin
  @Test
  void getAllFriendsAndIncomeInvitationsShouldReturnCorrectList(@Token String bearerToken) {
    List<UserJson> allFriends = gatewayApiClient.allFriends(bearerToken, null);
    Assertions.assertEquals(3, allFriends.size());
  }
}
