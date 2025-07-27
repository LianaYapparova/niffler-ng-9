package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;

import static guru.qa.niffler.jupiter.extension.UserExtension.DEFAULT_PASSWORD;

public class ArchiveCategoryTest {
    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            username = "duck5",
            categories = {
                    @Category(
                            name = "",
                            archived = false
                    )
            }
    )
    void activeCategoryInCategoryListTest(@Nonnull CategoryJson[] categoryJson) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage("duck5", DEFAULT_PASSWORD)
                .submit()
                .checkThatPageLoaded()
                .clickMenuButton()
                .clickProfileLink()
                .clickShowArchivedSwitch()
                .clickUnarchivedButton(categoryJson[0].name())
                .clickArchiveButton();
    }
}
