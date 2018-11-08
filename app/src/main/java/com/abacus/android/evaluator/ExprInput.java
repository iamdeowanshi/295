package com.abacus.android.evaluator;

import android.content.Context;

/**
 * Created by sunil28 on 11/5/18.
 */

public abstract class ExprInput {

    /**
     * build and return input
     * <p>
     * such as:
     * <p>
     * Solve(2x + 1, x)
     * Int(x, x)
     * D(2x + 3)
     * Int(sqrt(x), {x, 2, 4}
     *
     * @return - input
     */
    public abstract String getInput();




}

