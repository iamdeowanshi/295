package com.abacus.android.equation.evaluator.exceptions;

/**
 * Created by sunil28 on 11/5/18.
 */


public class NonBalanceBracketException extends ParsingException {

    public NonBalanceBracketException(String expr, int index) {
        super(expr, index);
    }

    @Override
    public String getMessage() {
        return "Non balance bracket of " + getExpr() + " at " + getIndex();
    }
}
