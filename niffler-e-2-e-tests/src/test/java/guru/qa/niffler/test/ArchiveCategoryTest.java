package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ArchiveCategoryTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            username = "duck",
            categories = {
                    @Category(
                            name = "",
                            archived = false
                    )
            }
    )
    void activeCategoryInCategoryListTest(CategoryJson categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck", "12345")
                .submit()
                .checkThatPageLoaded()
                .clickMenuButton()
                .clickProfileLink()
                .clickShowArchivedSwitch()
                .clickUnarchivedButton(categoryJson.name())
                .clickArchiveButton();
    }
}
