package com.abacus.android.service.latex;


import com.abacus.android.base.Presenter;

/**
 * Created by aaditya on 10/21/17.
 */

public interface LatexPresenter extends Presenter<LatexViewInteractor> {

    void getLatex(String base64);
}
