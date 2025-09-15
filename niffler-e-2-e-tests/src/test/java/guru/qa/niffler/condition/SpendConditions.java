package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(SpendJson... expectedSpends) {

        return new WebElementsCondition() {
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                return super.check(driver, elements);
            }

            @Override
            public String toString() {
                return null;
            }
        };

    }
}
