package az.siftoshka.habitube.di.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import az.siftoshka.habitube.model.server.HttpInterceptor;
import az.siftoshka.habitube.model.server.MovieDBApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static az.siftoshka.habitube.Constants.SYSTEM.API_URL;

public class ApiProvider implements Provider<MovieDBApi> {

    @Inject
    public ApiProvider() {
    }

    @Override
    public MovieDBApi get() {
        final OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(new HttpInterceptor());

        return new Retrofit.Builder()
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_URL)
                .build()
                .create(MovieDBApi.class);
    }
}
