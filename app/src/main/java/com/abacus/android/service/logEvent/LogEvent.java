package com.abacus.android.service.logEvent;

import com.abacus.android.base.Presenter;
import com.abacus.android.base.ViewInteractor;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public interface LogEvent extends Presenter<ViewInteractor> {

    void logEvent(Map<String, String> map);

}
