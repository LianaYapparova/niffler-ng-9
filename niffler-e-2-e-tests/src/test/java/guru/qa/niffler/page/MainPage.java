package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.StatComponent;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends ToolBar {
    private final SelenideElement spendingTable = $("#spendings");

    protected final StatComponent statComponent = new StatComponent();

    public MainPage checkThatPageLoaded() {
        spendingTable.should(visible);
        return this;
    }

    public EditSpendingPage editSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .$$("td")
                .get(5)
                .click();
        return new EditSpendingPage();
    }

    public MainPage checkThatTableContainsSpending(String description) {
        spendingTable.$$("tbody tr").find(text(description))
                .should(visible);
        return this;
    }

    @Nonnull
    public StatComponent getStatComponent() {
        return statComponent;
    }
}
