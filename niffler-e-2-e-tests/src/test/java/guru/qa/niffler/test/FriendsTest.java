package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

public class FriendsTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            friends = 1
    )
    void friendShouldBePresentInFriendsTableTest(@Nonnull UserJson user) {
        final UserJson friend = user.testData().friends().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .searchFriend(friend)
                .friendShouldBeVisible(friend);
    }

    @Test
    @User
    void friendShouldBeEmptyInFriendsTableTest(@Nonnull UserJson userJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(userJson.username(), userJson.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .friendShouldBeEmpty();
    }

    @Test
    @User(
            incomeInvitations = 1
    )
    void incomingRequestShouldBePresentTest(@Nonnull UserJson user) {
        final UserJson income = user.testData().incomeInvitations().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .clickMenuButton()
                .clickFriendsLink()
                .checkIncomingRequest(income);
    }
}
