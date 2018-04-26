package com.abacus.poc295.service;


import com.abacus.poc295.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by aaditya on 10/20/17.
 */

public class ApiModule {

    private static ApiModule apiModule;

    private ApiModule(){
        //Prevent form the reflection api.
        if (apiModule != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static ApiModule getInstance(){
        if (apiModule == null){ //if there is no instance available... create new one
            apiModule = new ApiModule();
        }

        return apiModule;
    }

    public AbacusService getWeatherService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.OPEN_WEATHER_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(AbacusService.class);
    }

    public PlacesService getPlacesService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.TIMEZONE_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(PlacesService.class);
    }



    public OkHttpClient provideOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

//        builder.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                HttpUrl url = request.url().newBuilder().addQueryParameter("appid",Config.KEY_).build();
//                request = request.newBuilder().url(url).build();
//                return chain.proceed(request);
//            }
//        });
        return builder.build();
    }

    public List<Interceptor> provideInterceptors() {
        List<Interceptor> interceptors = new ArrayList<>();
        // add header interceptor
        interceptors.add(getHeaderInterceptor());
        interceptors.add(new CustomeResponseInterceptor());

        // add logging interceptor
        if (Config.DEBUG) {
            interceptors.add(getLoggingInterceptor());
        }

        return interceptors;
    }

    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();

                Map<String, String> headers = getHeadersAfterAnnotatedSkip(chain.request().headers());
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    builder.addHeader(header.getKey(), header.getValue());
                }

                return chain.proceed(builder.build());
            }
        };
    }

    private Map<String, String> getHeadersAfterAnnotatedSkip(Headers annotatedHeaders) {
        Map<String, String> configHeaders = new HashMap<>(Config.API_HEADERS);

        for (String headerName : annotatedHeaders.names()) {
            configHeaders.remove(headerName);
        }

        return configHeaders;
    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

}