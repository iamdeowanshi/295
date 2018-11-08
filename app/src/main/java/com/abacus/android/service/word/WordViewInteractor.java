package com.abacus.android.service.word;

import com.abacus.android.base.ViewInteractor;
import com.abacus.android.model.WordProblem;

public interface WordViewInteractor extends ViewInteractor {

    void onSolution(WordProblem wordProblem);

}
