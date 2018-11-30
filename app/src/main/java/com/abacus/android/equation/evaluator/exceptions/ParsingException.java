package com.abacus.android.equation.evaluator.exceptions;

/**
 * Created by sunil28 on 11/5/18.
 */

public class ParsingException extends RuntimeException {

    private String expr;
    private int index;
    private String msg;

    public ParsingException(String expr) {

        this.expr = expr;
    }

    public ParsingException(String expr, int index) {

        this.expr = expr;
        this.index = index;
    }

    public ParsingException(String expr,String msg) {

        this.expr = expr;
        this.msg = msg;
    }

    public ParsingException(String expr, int index, String msg) {

        this.expr = expr;
        this.index = index;
        this.msg = msg;
    }

    public int getIndex() {
        return index;
    }

    public String getExpr() {
        return expr;
    }

    public String getMsg() {
        return msg;
    }
}