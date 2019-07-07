package az.amorphist.poster.di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import az.amorphist.poster.server.JsonPlaceHolderApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static az.amorphist.poster.App.apiUrl;

public class ApiProvider implements Provider<JsonPlaceHolderApi> {
    @Inject
    public ApiProvider() {
    }

    @Override
    public JsonPlaceHolderApi get() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiUrl)
                .build()
                .create(JsonPlaceHolderApi.class);
    }
}
