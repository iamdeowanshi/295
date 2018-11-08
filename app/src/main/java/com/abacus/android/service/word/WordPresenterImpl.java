package com.abacus.android.service.word;

import com.abacus.android.base.BasePresenter;
import com.abacus.android.model.WordProblem;
import com.abacus.android.service.ApiModule;
import com.abacus.android.service.LatexService;
import com.abacus.android.service.WordService;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class WordPresenterImpl extends BasePresenter<WordViewInteractor> implements WordPresenter {

    private WordService wordService = ApiModule.getInstance().getWordService();

    @Override
    public void getSolution(String question) {
        getViewInteractor().showProgress();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("question", question).build();
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("question", question);
        Observable<WordProblem> observable = wordService.getSolution(requestBody);
        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<WordProblem>() {
                    @Override
                    public void onNext(WordProblem response) {
                        getViewInteractor().onSolution(response);
                        getViewInteractor().hideProgress();

                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewInteractor().onError("Error occurred while processing your request. Try again");
                        getViewInteractor().hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }}));
    }
}
