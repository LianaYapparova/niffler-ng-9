package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.TYPE.*;

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            friends = 1
    )
    void friendShouldBePresentInFriendsTableTest(UserJson user) {
        final UserJson friend = user.testData().friends().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .friendShouldBeVisible(friend);
    }

    @Test
    @User()
    void friendShouldBeEmptyInFriendsTableTest(UserJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .friendShouldBeEmpty();
    }

    @Test
    @User(
            incomeInvitations = 1
    )
    void incomingRequestShouldBePresentTest(UserJson user) {
        final UserJson income = user.testData().incomeInvitations().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .checkIncomingRequest(income);
    }
}
