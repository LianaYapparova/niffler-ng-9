package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class AlertTest {
    private static final Config CFG = Config.getInstance();

    @User(
            categories = {
                    @Category(
                            name = ""
                    )
            }
    )
    @Test
    void checkAlertAfterArchiveCategoryTest(UserJson user) {
        final CategoryJson categoryJson = user.testData().categories().getFirst();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .checkThatPageLoaded()
                .clickMenuButton()
                .clickProfileLink()
                .clickArchivedButton(categoryJson.name())
                .clickArchiveButton()
                .checkAlert("Category " + categoryJson.name() + " is archived");
    }
}
