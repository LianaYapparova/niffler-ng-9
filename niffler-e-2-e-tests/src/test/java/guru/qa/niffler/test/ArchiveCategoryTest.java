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
            categories = {
                    @Category(
                            name = "",
                            archived = false
                    )
            }
    )
    void activeCategoryInCategoryListTest(CategoryJson[]  categoryJsons) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(categoryJsons[0].username(), "12345")
                .submit()
                .checkThatPageLoaded()
                .clickMenuButton()
                .clickProfileLink()
                .clickShowArchivedSwitch()
                .clickUnarchivedButton(categoryJsons[0].name())
                .clickArchiveButton();
    }
}
