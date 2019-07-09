package az.amorphist.poster.di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import az.amorphist.poster.server.MovieDBApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static az.amorphist.poster.App.apiUrl;

public class ApiProvider implements Provider<MovieDBApi> {
    @Inject
    public ApiProvider() {
    }

    @Override
    public MovieDBApi get() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiUrl)
                .build()
                .create(MovieDBApi.class);
    }
}
