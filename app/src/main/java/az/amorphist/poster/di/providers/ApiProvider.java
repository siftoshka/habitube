package az.amorphist.poster.di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import az.amorphist.poster.server.MovieDBApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static az.amorphist.poster.App.API_URL;

public class ApiProvider implements Provider<MovieDBApi> {
    @Inject
    public ApiProvider() {
    }

    @Override
    public MovieDBApi get() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_URL)
                .build()
                .create(MovieDBApi.class);
    }
}
