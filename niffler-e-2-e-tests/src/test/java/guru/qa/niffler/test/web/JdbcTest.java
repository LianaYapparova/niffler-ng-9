package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.User;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.*;


public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-2",
                "duck",
                false
            ),
            RUB,
            1000.0,
            "spend-name-tx",
            null
        )
    );

    System.out.println(spend);
  }

    @Test
    void createUserTest() {
        UserDbClient userDbClient = new UserDbClient();

        User user = userDbClient.createUser(
        new User(null, randomUsername(), randomPassword(),  RUB, randomName(),
                randomSurname(), null, null, null)
        );
        System.out.println(user);
    }
}
