package com.abacus.android.service.word;

import com.abacus.android.base.Presenter;
import com.abacus.android.model.WordProblem;

public interface WordPresenter extends Presenter<WordViewInteractor> {

    void getSolution(WordProblem problem);

    void sendFeedBack(WordProblem problem);

}
