package by.paranoidandroid.movielookup.service;

import java.io.IOException;

import by.paranoidandroid.movielookup.common.MyApplication;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final String QUERY_KEY_APIKEY = "apikey", QUERY_VALUE_APIKEY ="6ef5c24d",
                     QUERY_KEY_PAGE = "page";
        Request request = chain.request();
        HttpUrl url = chain.request().url()
                .newBuilder()
                .addQueryParameter(QUERY_KEY_APIKEY, QUERY_VALUE_APIKEY)
                .addQueryParameter(QUERY_KEY_PAGE, String.valueOf(MyApplication.getPage()))
                .build();
        MyApplication.nextPage();
        return chain.proceed(request.newBuilder().url(url).build());
    }
}