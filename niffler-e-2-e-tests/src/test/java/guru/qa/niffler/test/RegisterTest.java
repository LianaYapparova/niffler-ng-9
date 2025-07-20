package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class RegisterTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void registerNewUserTest() {
        UserJson newUser = new UserJson(null, randomUsername(), randomPassword(), null, null,
                null, null, null, null, null, null);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickRegisterButton()
                .setUserName(newUser.username())
                .setPassword(newUser.password())
                .setPasswordSubmit(newUser.password())
                .submit()
                .checkCongratulations();
    }
}
