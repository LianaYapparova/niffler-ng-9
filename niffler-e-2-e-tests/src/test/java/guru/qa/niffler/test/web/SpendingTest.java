package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(BrowserExtension.class)
public class SpendingTest {
    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    amount = 89990.00,
                    description = "Advanced 9 поток!",
                    category = "Обучение"
            )
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        final SpendJson spend = user.testData().spendings().getFirst();
        final String newDescription = ":)";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .checkThatPageLoaded()
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save()
                .checkThatTableContainsSpending(newDescription);
    }


    @User(
            spendings = @Spending(
                    amount = 89990.00,
                    description = "Advanced 9 поток!",
                    category = "Обучение"
            )
    )
    @ScreenShotTest(value = "img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        StatComponent statComponent = Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .getStatComponent();

        BufferedImage actual = ImageIO.read($("canvas[role='img']").screenshot());
        assertFalse(new ScreenDiffResult(
                expected,
                actual
        ));

        statComponent.checkBubbles(Color.green);
    }
}
