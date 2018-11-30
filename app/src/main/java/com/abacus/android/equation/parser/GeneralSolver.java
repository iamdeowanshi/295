package com.abacus.android.equation.parser;

import android.content.Context;
import android.widget.Toast;

import com.abacus.android.equation.evaluator.FormatExpression;
import com.abacus.android.equation.evaluator.exceptions.EmptyInputExpressionException;
import com.abacus.android.equation.evaluator.exceptions.ExpressionChecker;
import com.abacus.android.model.SolveItem;

import static com.abacus.android.equation.parser.CalculusExample.main;

public class GeneralSolver {


    public GeneralSolver(){

    }

    public String getSymjaInput(String expr){
        if(!expr.isEmpty()){
            ExpressionChecker.checkExpression(expr);
            String formattedExpr = getFormattedExpression(expr);
            return main(formattedExpr);

        } else {
            throw new EmptyInputExpressionException(expr);
        }
    }

    public boolean isEmpty(String expr){
        return expr.isEmpty();
    }

    protected String getExpression(String expr) {
        SolveItem solveItem = new SolveItem(expr);
        return solveItem.getInput();
    }

    public String getFormattedExpression(String expr){
        return FormatExpression.appendParenthesis(expr);
    }

    public boolean checkSymjaExpression(String expr, Context context){
        if (expr == null) {
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }


}