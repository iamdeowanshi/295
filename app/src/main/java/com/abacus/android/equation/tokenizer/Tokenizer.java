package com.abacus.android.equation.tokenizer;

import com.abacus.android.equation.evaluator.Constants;

import java.util.ArrayList;


/**
 * Created by sunil28 on 11/6/18.
 */

public class Tokenizer {
    private final ArrayList<Localize> mReplacements;
    private String TAG = Tokenizer.class.getName();

    public Tokenizer() {
        mReplacements = new ArrayList<>();
    }

    private void generateReplacements() {
        mReplacements.clear();
        mReplacements.add(new Localize("/", "÷"));
        mReplacements.add(new Localize("*", "×"));
        mReplacements.add(new Localize("-", "-"));
        mReplacements.add(new Localize("-", "\u2010"));
        mReplacements.add(new Localize("-", "\u2012"));
        mReplacements.add(new Localize("-", "\u2212"));
        mReplacements.add(new Localize("-", "\u2796"));

        mReplacements.add(new Localize("cbrt", "³√"));
        mReplacements.add(new Localize("infinity", Character.toString(Constants.INFINITY_UNICODE)));
        mReplacements.add(new Localize("sqrt", "√"));
        mReplacements.add(new Localize("<=", "≤"));
        mReplacements.add(new Localize(">=", "≥"));
        mReplacements.add(new Localize("!=", "≠"));

        mReplacements.add(new Localize("(pi)", "π"));
        mReplacements.add(new Localize("(degree)", "°"));

        //re translate
        mReplacements.add(new Localize("pi", "π"));
        mReplacements.add(new Localize("degree", "°"));
    }

    /**
     * translate to english
     *
     * @param expr - local language
     * @return - english
     */
    public String getNormalExpression(String expr) {
        generateReplacements();
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.local, replacement.english);
        }
        return expr;
    }

    /**
     * translate to local language
     *
     * @param expr - english
     * @return - local language
     */
    public String getLocalizedExpression(String expr) {
        generateReplacements();
        for (Localize replacement : mReplacements) {
            expr = expr.replace(replacement.english, replacement.local);
        }
        return expr;
    }


    private class Localize {
        String english;
        String local;

        Localize(String english, String local) {
            this.english = english;
            this.local = local;
        }
    }
}
