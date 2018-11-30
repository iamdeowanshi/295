package com.abacus.android.service;

import com.abacus.android.model.WordProblem;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Student on 10/24/17.
 */

public interface WordService {

    @POST("/wordproblem/solve")
    Observable<WordProblem> getSolution(@Body WordProblem wordProblem);

    @POST("/wordproblem/feedback")
    Observable<Void> sendFeedBack(@Body WordProblem problem);
}