package guru.qa.niffler.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UserApi {

    @GET("register")
    Call<Void> requestRegisterForm();

    @POST("register")
    Call<Void> register(@Body UserApiClient.LoginRequest loginRequest);
}
