package guru.qa.niffler.test;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendshipStatus;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@RestTest
public class FriendsV2RestTest {

  @RegisterExtension
  private static final ApiLoginExtension apiLogin = ApiLoginExtension.rest();

  private final GatewayV2ApiClient gatewayApiClient = new GatewayV2ApiClient();

  @ApiLogin(username = "duck", password = "12345")
  @Test
  void getAllFriendsAndIncomeInvitationsShouldReturnCorrectList(@Token String bearerToken) {
    final Page<UserJson> allFriends = gatewayApiClient.allFriends(bearerToken, 0, 10, "username,DESC", null);
    final List<UserJson> content = allFriends.getContent();
    Assertions.assertEquals(2, content.size());
    Assertions.assertEquals("barsik", content.get(0).username());
  }
}
