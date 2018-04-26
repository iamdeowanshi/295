package com.abacus.poc295.service.Forecast;


import com.abacus.poc295.LatexResponse;
import com.abacus.poc295.base.ViewInteractor;

/**
 * Created by aaditya on 10/21/17.
 */

public interface LatexViewInteractor extends ViewInteractor {

    void showProgress();

    void hideProgress();

    void onLatexResult(LatexResponse result);

}
