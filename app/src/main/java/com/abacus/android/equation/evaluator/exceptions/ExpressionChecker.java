package com.abacus.android.equation.evaluator.exceptions;

import java.util.Stack;

/**
 * Created by sunil28 on 11/7/18.
 */

public class ExpressionChecker {

    public static void checkBalanceBracket(String expr) throws NonBalanceBracketException {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') {
                stack.push(expr.charAt(i));
            } else if (expr.charAt(i) == ')') {
                if (stack.isEmpty()) throw new NonBalanceBracketException(expr, i);
                Character open = stack.pop();
                if (open != '(') {
                    throw new NonBalanceBracketException(expr, i);
                }
            }
        }
    }


    public static void checkExpression(String expr) {
        checkBalanceBracket(expr);
    }
}
