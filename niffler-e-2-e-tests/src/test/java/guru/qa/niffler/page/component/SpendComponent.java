package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class SpendComponent extends BaseComponent<SpendComponent>{
    protected SpendComponent(SelenideElement self) {
        super($("#stat"));
    }
}
