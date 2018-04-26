package com.abacus.poc295.service;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Student on 10/24/17.
 */

public interface PlacesService {

    @GET("autocomplete/json")
    Observable<ResponseBody> getPrediction(String input, String key, String types);

    @GET("details/json")
    Observable<ResponseBody> getLatLong(String placeid, String key);

    @GET("get-time-zone")
    Observable<ResponseBody> getTimeZoneForCity(@Query("key") String key, @Query("format") String format, @Query("lat") String lat, @Query("lng") String lng, @Query("by") String by);


}
