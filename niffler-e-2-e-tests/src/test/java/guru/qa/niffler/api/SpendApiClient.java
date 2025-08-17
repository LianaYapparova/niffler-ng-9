package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.RestClient;
import lombok.SneakyThrows;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient extends RestClient {

    private static final Config CFG = Config.getInstance();

    private final SpendApi spendApi;

    public SpendApiClient() {
        super(CFG.spendUrl());
        this.spendApi = create(SpendApi.class);
    }


    public SpendJson addSpend(SpendJson spendJson) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spendJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }

        assertEquals(201, response.code());
        return response.body();
    }

    public CategoryJson addCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(categoryJson)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }
}
