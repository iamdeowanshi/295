package com.abacus.android.parser;

import android.content.Context;
import android.widget.Toast;

import com.abacus.android.evaluator.exceptions.EmptyInputExpressionException;
import com.abacus.android.evaluator.exceptions.ExpressionChecker;
import com.abacus.android.model.DerivativeItem;

import org.matheclipse.core.eval.util.SolveUtils;


import static com.abacus.android.parser.CalculusExample.main;

/**
 * Created by sunil28 on 11/5/18.
 */

public class DerivativeEvaluator {

    public String getSymjaInput(String expr){
        if(!expr.isEmpty()){

            String derivativeExpression = null;
            try {
                derivativeExpression = getExpression(expr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return main(derivativeExpression);
        } else {
            throw new EmptyInputExpressionException(expr);
        }
    }

    public boolean isEmpty(String expr){
        return expr.isEmpty();
    }

    protected String getExpression(String expr) throws Exception {
        int level=0;
        String operator = "\\frac { d } { d x }";

        System.out.println("Input : " + expr);
        if(!expr.contains(operator))
            throw new Exception("This is a not a valid question.");

        while(expr.contains(operator)){
            expr=expr.substring(operator.length()).trim();
            level++;
        }


        expr=removeClutter(expr);
        try {
            ExpressionChecker.checkExpression(expr);
        } catch (Exception e) {
            throw new Exception("Expression field is malformed");
        }


        System.out.println("Level : "+level);
        System.out.println("expression : "+expr);

        DerivativeItem derivativeItem = new DerivativeItem(expr, "x", String.valueOf(level));
        return derivativeItem.getInput();
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

        if(expr.contains("("))
            expr=expr.replaceAll("\\(","");

        if(expr.contains(")"))
            expr=expr.replaceAll("\\)","");

        if(expr.contains(" "))
            expr=expr.replaceAll(" ","");


        return expr.trim();
    }

}
