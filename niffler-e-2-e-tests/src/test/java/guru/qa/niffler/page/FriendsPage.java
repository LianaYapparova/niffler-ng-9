package guru.qa.niffler.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.model.UserJson;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage extends BasePage<FriendsPage> implements Checked<FriendsPage> {
    private final SelenideElement friendsTable = $(By.id("friends"));
    private final SelenideElement friendsRequestTable = $(By.id("requests"));
    @Override
    public FriendsPage checkOpen() {
        $("[id='simple-tabpanel-friends'] h2").shouldBe(visible, exactText("My friends\n"));
        return this;
    }

    public FriendsPage friendShouldBeVisible(UserJson friend) {
        friendsTable.findAll("td").findBy(Condition.innerText(friend.username())).shouldBe(visible);
        return this;
    }

    public FriendsPage friendShouldBeEmpty() {
        friendsTable.shouldBe(not(exist));
        return this;
    }

    public FriendsPage checkIncomingRequest(UserJson user) {
        friendsRequestTable.findAll("td").findBy(Condition.innerText(user.username())).shouldBe(visible);
        return this;
    }
}
