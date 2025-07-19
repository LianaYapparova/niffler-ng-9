package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.data.entity.user.CurrencyValues.RUB;
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

        UserJson user = userDbClient.createUser(
        new UserJson(null, randomUsername(), randomPassword(),  RUB, randomName(),
                randomSurname(), null, null, null)
        );
        System.out.println(user);

        UserJson user2 = userDbClient.createUserSpringJdbc(
                new UserJson(null, randomUsername(), randomPassword(),  RUB, randomName(),
                        randomSurname(), null, null, null)
        );
        System.out.println(user2);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                new UserJson(null, randomUsername(), randomPassword(),  RUB, randomName(),
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
