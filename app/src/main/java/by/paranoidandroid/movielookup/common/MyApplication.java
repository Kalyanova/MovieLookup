package by.paranoidandroid.movielookup.common;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import by.paranoidandroid.movielookup.service.OkHttpInterceptor;
import by.paranoidandroid.movielookup.service.OmdbService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {
    static final String BASE_URL = "http://omdbapi.com";
    static OmdbService api;
    static int page = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpInterceptor queryInterceptor = new OkHttpInterceptor();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(queryInterceptor)
                .addInterceptor(logInterceptor);

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();

        api = retrofit.create(OmdbService.class);
    }

    public static OmdbService getApi() {
        return api;
    }

    public static int getPage() {
        return page;
    }

    public static void nextPage() {
        ++page;
    }

    public static void startPage() {
        page = 1;
    }
}
