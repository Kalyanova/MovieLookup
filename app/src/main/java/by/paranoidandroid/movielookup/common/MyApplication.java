package by.paranoidandroid.movielookup.common;

import android.app.Application;

import by.paranoidandroid.movielookup.service.OmdbService;

public class MyApplication extends Application {
    static OmdbService api;
    static int page = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        api = ApiClient.getClient().create(OmdbService.class);
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
