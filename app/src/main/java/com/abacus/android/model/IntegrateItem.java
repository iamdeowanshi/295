package com.abacus.android.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abacus.android.evaluator.Constants;
import com.abacus.android.evaluator.ExprInput;
import com.abacus.android.evaluator.FormatExpression;


/**
 * Created by sunil28 on 11/7/18.
 */


public class IntegrateItem extends ExprInput {
    protected final String var = "x";
    protected String from, to;
    protected String input;

    public IntegrateItem(@NonNull String input, @NonNull String f, @NonNull String t) {
        this.from = FormatExpression.cleanExpression(f);
        this.to = FormatExpression.cleanExpression(t);
        this.input = FormatExpression.cleanExpression(input);
    }

//    @Override
//    public String getError(MathEvaluator evaluator, Context applicationContext) {
//        return null;
//    }

    public String getVar() {
        return var;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

//    @Override
//    public boolean isError(MathEvaluator evaluator) {
//        return false;
//    }

    @Override
    public String toString() {
        return "Integrate " + input +
                Constants.FROM + " " + from + " " +
                Constants.TO + " " + to;
    }

    public String getInput() {
        return "Int(" + input + ",{" + var + "," + from + "," + to + "})";
    }

    public void setInput(String input) {
        this.input = input;
    }
}

