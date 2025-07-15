package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
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
                randomCategoryName(),
                "duck",
                false
            ),
            RUB,
            1000.0,
            "spend-name-tx",
                "duck"
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

        User user2 = userDbClient.createUserSpringJdbc(
                new User(null, randomUsername(), randomPassword(),  RUB, randomName(),
                        randomSurname(), null, null, null)
        );
        System.out.println(user2);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        User user = usersDbClient.createUserSpringJdbc(
                new User(null, randomUsername(), randomPassword(),  RUB, randomName(),
                        randomSurname(), null, null, null)
        );
        System.out.println(user);
    }

    @Test
    void createSpendJdbcSpringTest() {
        SpendDaoSpringJdbc spendDaoSpringJdbc = new SpendDaoSpringJdbc();
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpendJdBcSpring(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                randomCategoryName(),
                                "duck",
                                false
                        ),
                        RUB,
                        1000.0,
                        "spend-name-tx",
                        "duck"
                )
        );

        System.out.println(spend);
        System.out.println((long) spendDaoSpringJdbc.findAll().size());
        System.out.println((long) spendDaoSpringJdbc.findAllByUserName("duck").size());
    }
}
