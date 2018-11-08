package com.abacus.android.model;


import com.abacus.android.evaluator.Constants;
import com.abacus.android.evaluator.ExprInput;
import com.abacus.android.evaluator.FormatExpression;

/**
 * Created by sunil28 on 11/5/18.
 */

public class SolveItem extends ExprInput {
    private String leftExpr = "x";
    private String rightExpr = "0";

    private String var = "x";

    public SolveItem(String inp) {
        processInput(inp);
    }

    public SolveItem(String leftExpr, String rightExpr) {
        this.leftExpr = FormatExpression.appendParenthesis(leftExpr);
        this.rightExpr = FormatExpression.appendParenthesis(rightExpr);
    }

    /**
     * process input
     *
     * @param inp - expression
     */
    private void processInput(String inp) {
        while (inp.contains("==")) inp = inp.replace("==", "="); //clear ==
        if (inp.contains("X")) {
            var = "X";
        } else {
            var = "x";
        }

        //2x + 1 = 2 ....
        if (inp.contains("=")) {
            String[] s = inp.split("=");
            if (s.length >= 2) { //"2x + 1 = 2 = 3" -> only use "2x + 1 = 2"
                if (leftExpr.isEmpty()) leftExpr = "0";
                leftExpr = FormatExpression.appendParenthesis(s[0]);
                if (rightExpr.isEmpty()) rightExpr = "0";
                rightExpr = FormatExpression.appendParenthesis(s[1]);
            } else { //"2x + 1 =" -> index exception because length s = 1
                leftExpr = FormatExpression.appendParenthesis(s[0]);
                rightExpr = "0";
            }
        } else { // 2x + 1
            leftExpr = FormatExpression.appendParenthesis(inp);
            rightExpr = "0";
        }
    }

    public String getLeftExpr() {
        return leftExpr;
    }

    public void setLeftExpr(String leftExpr) {
        this.leftExpr = leftExpr;
    }

    public String getRightExpr() {
        return rightExpr;
    }

    public void setRightExpr(String rightExpr) {
        this.rightExpr = rightExpr;
    }


    public String getInput() {
        return Constants.SOLVE + Constants.LEFT_PAREN
                + leftExpr + " == " + rightExpr + " ," + var +
                Constants.RIGHT_PAREN;
    }

    @Override
    public String toString() {
        return this.leftExpr + Character.toString(Constants.EQUAL_UNICODE) + this.rightExpr;
    }

    /**
     * return input expression
     *
     * @return - String
     */
    public String getExpr() {
        return this.leftExpr + "==" + rightExpr;
    }
}
