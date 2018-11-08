package com.abacus.android.parser;

import android.content.Context;
import android.widget.Toast;

import com.abacus.android.evaluator.exceptions.EmptyInputExpressionException;
import com.abacus.android.evaluator.exceptions.ExpressionChecker;
import com.abacus.android.model.SolveItem;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Stack;

import static com.abacus.android.parser.CalculusExample.main;


/**
 * Created by sunil28 on 11/5/18.
 */

public class QuadraticEquationEvaluator {


    public String getSymjaInput(String expr){
        if(!expr.isEmpty()){
            ExpressionChecker.checkExpression(expr);
            expr = removeClutter(expr);
            SolveItem solveItem = new SolveItem(expr);
            return main(solveItem.getInput());

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

    public boolean checkSymjaExpression(String expr, Context context){
        if (expr == null) {
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
            return false;
        } else return true;
    }

    public String removeClutter(String expr){

        if(expr.contains("{"))
            expr=expr.replaceAll("\\{","");

        if(expr.contains("}"))
            expr=expr.replaceAll("\\}","");

        if(expr.contains(" "))
            expr=expr.replaceAll(" ","");


        return expr.trim();
    }
}
