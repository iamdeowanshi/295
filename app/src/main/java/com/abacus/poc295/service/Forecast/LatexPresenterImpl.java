package com.abacus.poc295.service.Forecast;

import com.abacus.poc295.Config;
import com.abacus.poc295.LatexResponse;
import com.abacus.poc295.base.BasePresenter;
import com.abacus.poc295.service.AbacusService;
import com.abacus.poc295.service.ApiModule;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class LatexPresenterImpl  extends BasePresenter<LatexViewInteractor> implements LatexPresenter {

    private AbacusService abacusService = ApiModule.getInstance().getAbacusService();

    @Override
    public void getLatex(String base64) {
        getViewInteractor().showProgress();
        HashMap<String, String> bodyMap = new HashMap<>();
        bodyMap.put("src", "data:image/jpeg;base64," + base64);
        Observable<LatexResponse> observable = abacusService.getLatex(bodyMap,Config.APP_ID, Config.KEY_);
        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LatexResponse>() {
                    @Override
                    public void onNext(LatexResponse latexResponse) {

                        if (latexResponse.getLatex_confidence() > 0.5) {
                            getViewInteractor().onLatexResult(latexResponse);
                            getViewInteractor().hideProgress();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }}));
    }
}
