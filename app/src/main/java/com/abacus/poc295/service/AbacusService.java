package com.abacus.poc295.service;

import com.abacus.poc295.LatexResponse;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by aaditya on 10/19/17.
 */

public interface AbacusService {

    @POST("latex")
    Observable<LatexResponse> getLatex(@Body HashMap<String, String> src, @Header("app_id") String appId, @Header("app_key") String key);


}
