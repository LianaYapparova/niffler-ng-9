package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.allure.Bubble;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color " + expectedColor.rgb) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition color(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {

            private final String expectedRgba = Arrays.stream(expectedBubbles).map(c -> c.color().rgb).toList().toString();
            private final String expectedText = Arrays.stream(expectedBubbles).map(c -> c.text()).toList().toString();
            private String message = "";

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                final List<String> actualTextList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedBubbles[i].color();
                    final String text = expectedBubbles[i].text();
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String textActual = elementToCheck.getText();
                    actualRgbaList.add(rgba);
                    actualTextList.add(textActual);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba) && text.equals(textActual);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String actualText = actualTextList.toString();
                    message = String.format(
                            "List colors mismatch (expected: %s, actual: %s) and List texts mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba,
                            expectedText, actualText
                    );
                    return rejected(message, actualRgba + "   " + actualText);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return message;
            }
        };
    }
}
