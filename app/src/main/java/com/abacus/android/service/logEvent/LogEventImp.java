package com.abacus.android.service.logEvent;

import com.abacus.android.base.BasePresenter;
import com.abacus.android.base.ViewInteractor;
import com.abacus.android.model.User;
import com.abacus.android.model.WordProblem;
import com.abacus.android.service.ApiModule;
import com.abacus.android.service.LogService;
import com.abacus.android.service.WordService;
import com.abacus.android.util.PreferenceUtil;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class LogEventImp extends BasePresenter<ViewInteractor> implements LogEvent {

    private LogService logService = ApiModule.getInstance().getLogService();

    @Override
    public void logEvent(Map<String, String> map) {
        Observable<Void> observable = logService.logEvent(map);
        new CompositeDisposable().add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Void>() {
                    @Override
                    public void onNext(Void aVoid) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

}
