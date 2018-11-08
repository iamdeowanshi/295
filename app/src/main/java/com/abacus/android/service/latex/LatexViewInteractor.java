package com.abacus.android.service.latex;


import com.abacus.android.model.LatexResponse;
import com.abacus.android.model.WordProblem;
import com.abacus.android.base.ViewInteractor;

/**
 * Created by aaditya on 10/21/17.
 */

public interface LatexViewInteractor extends ViewInteractor {

    void onLatexResult(LatexResponse result);

}
