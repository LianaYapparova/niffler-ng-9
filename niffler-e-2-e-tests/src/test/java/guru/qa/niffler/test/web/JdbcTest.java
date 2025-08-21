package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.spend.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.repository.userdata.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.jupiter.extension.ClientResolver;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Date;

import static guru.qa.niffler.data.entity.user.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.*;

@ExtendWith(ClientResolver.class)
public class JdbcTest {

    private SpendClient spendClient;
    private UsersClient usersClient;

    @Test
    void txTest() {

        String userName = randomUsername();

        SpendJson spend = spendClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                randomCategoryName(),
                                userName,
                                false
                        ),
                        RUB,
                        1000.0,
                        "spend-name-tx",
                        userName
                )
        );

        System.out.println(spend);
    }

    @Test
    void createUserTest() {

        UserJson user = usersClient.createUser(
                new UserJson(null, randomUsername(), randomPassword(), RUB, randomName(),
                        randomSurname(), null, null, null, null, null, null)
        );
        System.out.println(user);

        UserJson user2 = usersClient.createUserSpringJdbc(
                new UserJson(null, randomUsername(), randomPassword(), RUB, randomName(),
                        randomSurname(), null, null, null, null, null, null)
        );
        System.out.println(user2);
    }

    @Test
    void createUserWithFriendshipTest() {

        UserJson user = usersClient.createUser(
                new UserJson(null, randomUsername(), randomPassword(), RUB, randomName(),
                        randomSurname(), null, null, null, null, null, null)
        );
        System.out.println(user);

        UserJson user2 = usersClient.createUserHibernate(
                new UserJson(null, randomUsername(), randomPassword(), RUB, randomName(),
                        randomSurname(), null, null, null, null, null, null)
        );
        System.out.println(user2);
        UserdataUserRepositoryHibernate userDataRepositoryJdbc = new UserdataUserRepositoryHibernate();
        userDataRepositoryJdbc.findById(user2.id()).get();

        usersClient.addIncomeInvitation(user2, 1);
        usersClient.addOutcomeInvitation(user2, 1);
        usersClient.addFriend(user, 1);
    }

    @Test
    void springJdbcTest() {
        UserDbClient usersDbClient = new UserDbClient();
        UserJson user = usersDbClient.createUserSpringJdbc(
                new UserJson(null, randomUsername(), randomPassword(), RUB, randomName(),
                        randomSurname(), null, null, null, null, null, null)
        );
        System.out.println(user);
    }

    @Test
    void createSpendJdbcSpringTest() {
        SpendDaoSpringJdbc spendDaoSpringJdbc = new SpendDaoSpringJdbc();
        SpendDbClient spendDbClient = new SpendDbClient();
        String userName = randomUsername();
        SpendJson spend = spendDbClient.createSpendJdBcSpring(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                randomCategoryName(),
                                userName,
                                false
                        ),
                        RUB,
                        1000.0,
                        "spend-name-tx",
                        userName
                )
        );

        System.out.println(spend);
        System.out.println((long) spendDaoSpringJdbc.findAll().size());
        System.out.println((long) spendDaoSpringJdbc.findAllByUserName("duck").size());
    }
}
