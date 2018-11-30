package com.abacus.android.service;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LogService {

    @POST("analytics/kafka/publish")
    Observable<Void> logEvent(@Body Map<String, String> body);

}
