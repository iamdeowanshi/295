package com.abacus.android.service.word;

import com.abacus.android.base.Presenter;

public interface WordPresenter extends Presenter<WordViewInteractor> {

    void getSolution(String question);
}
