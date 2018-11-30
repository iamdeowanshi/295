package com.abacus.android.equation.evaluator.exceptions;

/**
 * Created by sunil28 on 11/5/18.
 */

public class EmptyInputExpressionException extends ParsingException {

    public EmptyInputExpressionException(String expr) {
        super(expr);
    }

    @Override
    public String getMessage() {
        return "The given input is empty!!!";
    }
}