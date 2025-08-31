package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.service.impl.UserDbClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.data.entity.user.CurrencyValues.RUB;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class LoginTest {

    private static final Config CFG = Config.getInstance();
    private UserApiClient userApiClient = new UserApiClient();
    private UserDbClient userDbClient =  new UserDbClient();

    @Test
    void loginTest() {
        UserJson newUser = new UserJson(null, randomUsername(), randomPassword(), RUB, null,
                null, null, null, null, null, null, null);
        userApiClient.registerUser(newUser);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(newUser.username(), newUser.password())
                .submit();
    }
}
