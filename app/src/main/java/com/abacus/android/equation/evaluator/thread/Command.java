package com.abacus.android.equation.evaluator.thread;

/**
 * Created by sunil28 on 11/5/18.
 */

/**
 * Uses the Command Design Pattern to effectively have lambda expressions in a pre-Java 8 environment.
 * Note: e is the return Object, F is the param Object.
 *
 * @author Alston Lin
 * @version 3.0
 */
public interface Command<RETURN, INPUT> {
    public RETURN execute(INPUT input);
}
