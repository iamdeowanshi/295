package com.abacus.android.service;

import com.abacus.android.model.LatexResponse;
import com.abacus.android.model.WordProblem;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by aaditya on 10/19/17.
 */

public interface LatexService {

    @POST("latex")
    Observable<LatexResponse> getLatex(@Body HashMap<String, String> src, @Header("app_id") String appId, @Header("app_key") String key);




}
