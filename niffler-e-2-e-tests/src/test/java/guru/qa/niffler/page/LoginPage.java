package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $("[id='register-button']");

    @Nonnull
    public LoginPage fillLoginPage(String username, String password) {
        usernameInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        return this;
    }

    public MainPage submit() {
        submitButton.click();
        return new MainPage();
    }

    public RegisterPage clickRegisterButton() {
        registerButton.shouldBe(enabled).click();
        return page(RegisterPage.class).checkOpen();
    }
}
