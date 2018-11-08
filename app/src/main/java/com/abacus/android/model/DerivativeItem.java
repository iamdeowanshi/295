package com.abacus.android.model;

import android.content.Context;
import android.util.Log;

import com.abacus.android.evaluator.Constants;
import com.abacus.android.evaluator.ExprInput;
import com.abacus.android.evaluator.FormatExpression;


/**
 * Created by sunil28 on 11/7/18.
 */


public class DerivativeItem extends ExprInput {
    private static final String TAG = "DerivativeItem";
    private String input;
    private String var = "x";
    private String level = "1";

    public DerivativeItem(String input, String var) {
        this.input = FormatExpression.cleanExpression(input);
        //if var = "", do not set var
        if (!var.isEmpty()) this.var = var;
    }

    public DerivativeItem(String input, String var, String level) {
        this.input = FormatExpression.cleanExpression(input);
        if (!var.isEmpty()) this.var = var;     //if var = "", do not set var
        this.level = level;
    }

    public DerivativeItem(String input) {
        this.input = FormatExpression.cleanExpression(input);
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }


    public String getInput() {
        //build mResult
        String res = Constants.DERIVATIVE +
                Constants.LEFT_PAREN +
                input + "," + "{" + var + "," + level + "}" +
                Constants.RIGHT_PAREN;
        //pri(2x + sin(x), x)
        Log.d(TAG, "getInput: " + res);
        return res;
    }

    @Override
    public String toString() {
        return this.getInput();
    }


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
