package com.abacus.poc295.service.Forecast;


import com.abacus.poc295.base.Presenter;

/**
 * Created by aaditya on 10/21/17.
 */

public interface LatexPresenter extends Presenter<LatexViewInteractor> {

    void getLatex(String base64);
}
