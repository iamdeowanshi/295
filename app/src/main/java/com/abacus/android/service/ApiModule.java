package com.abacus.android.service;


import com.abacus.android.Config;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

    public LatexService getLatexService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LATEX_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addConverterFactory(provideGsonConverterFactory(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(LatexService.class);
    }

    public WordService getFeedbackService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.WORD_PROBLEM_FEEDBACK)
                .client(provideOkHttpClient(provideInterceptors()))
                .addConverterFactory(provideGsonConverterFactory(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(WordService.class);
    }

    public Converter.Factory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
    public WordService getWordService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.WORD_PROBLEM_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addConverterFactory(provideGsonConverterFactory(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(WordService.class);
    }



    public OkHttpClient provideOkHttpClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30,TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

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

    public LogService getLogService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.LOGGING_URL)
                .client(provideOkHttpClient(provideInterceptors()))
                .addConverterFactory(provideGsonConverterFactory(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(LogService.class);    }
}