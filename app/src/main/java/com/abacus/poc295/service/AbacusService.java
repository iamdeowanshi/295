package com.abacus.poc295.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by aaditya on 10/19/17.
 */

public interface AbacusService {

    @GET("forecast")
    Observable<ResponseBody> get24HrForecast(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String key);


}
