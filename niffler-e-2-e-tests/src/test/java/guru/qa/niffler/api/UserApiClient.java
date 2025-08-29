package guru.qa.niffler.api;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.RestClient;
import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class UserApiClient extends RestClient {

    private final UserApi userApi;

    public UserApiClient() {
        super(CFG.authUrl());
        this.userApi = create(UserApi.class);
    }


    @Step("Create user using API")
    public void registerUser(UserJson userJson) {
        try {
            userApi.requestRegisterForm().execute();
            userApi.register(
                    new LoginRequest(userJson.username(),
                            userJson.password(),
                            userJson.password(),
                            ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"))
            ).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @AllArgsConstructor
    @Setter
    @Getter
    public class LoginRequest {
        private String username;
        private String password;
        private String passwordSubmit;
        private String _csrf;
    }

}
