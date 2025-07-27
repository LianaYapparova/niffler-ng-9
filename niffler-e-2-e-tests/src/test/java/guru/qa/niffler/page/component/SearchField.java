package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class SearchField {

    private final SelenideElement input = $(".MuiInputBase-root input");
    private final SelenideElement clear = $(".MuiBox-root [id='input-clear']");

    public SearchField search(String query) {
        input.setValue(query).sendKeys(Keys.ENTER);
        return null;
    }

    public SearchField clearIfNotEmpty(){
        clear.click();
        return this;
    }
}
