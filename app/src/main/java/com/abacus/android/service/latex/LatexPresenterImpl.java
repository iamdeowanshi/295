package com.abacus.android.service.latex;

import com.abacus.android.Config;
import com.abacus.android.model.LatexResponse;
import com.abacus.android.model.WordProblem;
import com.abacus.android.base.BasePresenter;
import com.abacus.android.service.LatexService;
import com.abacus.android.service.ApiModule;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class LatexPresenterImpl  extends BasePresenter<LatexViewInteractor> implements LatexPresenter {

    private LatexService latexService = ApiModule.getInstance().getLatexService();

    @Override
    public void getLatex(String base64) {
        getViewInteractor().showProgress();
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("src", "data:image/jpeg;base64," + base64);
        Observable<LatexResponse> observable = latexService.getLatex(bodyMap,Config.APP_ID, Config.KEY_);
        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LatexResponse>() {
                    @Override
                    public void onNext(LatexResponse latexResponse) {
                        getViewInteractor().hideProgress();

                        if (latexResponse.getLatex_confidence() > 0.5) {
                            getViewInteractor().onLatexResult(latexResponse);
                        } else {
                            getViewInteractor().onError("Can't process Image");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewInteractor().onError("can't process");
                        getViewInteractor().hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }}));
    }



}
