package com.abacus.android.equation.parser;

import android.content.Context;
import android.widget.Toast;

import com.abacus.android.equation.evaluator.exceptions.EmptyInputExpressionException;
import com.abacus.android.equation.evaluator.exceptions.ExpressionChecker;
import com.abacus.android.model.IntegrateItem;

import static com.abacus.android.equation.parser.CalculusExample.main;


/**
 * Created by sunil28 on 11/5/18.
 */

public class IntegrationEvaluator {

    public String getSymjaInput(String expr){
        if(!expr.isEmpty()){

            String integrationExpression = null;
            try {
                integrationExpression = getExpression(expr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return main(integrationExpression);
        } else {
            throw new EmptyInputExpressionException(expr);
        }
    }

    public boolean isEmpty(String expr){
        return expr.isEmpty();
    }

    protected String getExpression(String expr) throws Exception {

        if(!expr.contains("_"))
            throw new Exception("From & To fields are missing ?");

        boolean fStart=false;
        boolean fEnd=false;

        boolean tStart=false;
        boolean tEnd=false;

        int fromStart=0;
        int fromEnd=0;
        int toStart=0;
        int toEnd=0;

        //\frac { d } { d x } x ^ { 2 }
        //\int x ^ { 2 } d xSubSubwq
        //\int _ { 0 } ^ { 1 } x ^ { 2 } d x

        for(int i=0; i<expr.length() && !tEnd ;i++){
            char ch = expr.charAt(i);
            if(ch == '{' && !fStart){
                fromStart=i;
                fStart=true;
            }


            if(ch == '}' && fStart && !fEnd){
                fromEnd=i;
                fEnd=true;
            }


            if(ch == '{' && fStart && fEnd && !tStart){
                toStart=i;
                tStart=true;
            }


            if(ch == '}' && fStart && fEnd && tStart && !tEnd){
                toEnd=i;
                tEnd=true;
            }
        }

        String fromLimit= removeClutter(expr.substring(fromStart,fromEnd).trim());
        String toLimit= removeClutter(expr.substring(toStart,toEnd).trim());
        String expression = removeClutter(expr.substring(toEnd+1,expr.length()-3));
        String variable = removeClutter(Character.toString(expr.charAt(expr.length()-1)));
        System.out.println("fromLimit : "+fromLimit);
        System.out.println("toLimit : "+toLimit);
        System.out.println("expression : "+expression);
        System.out.println("variable : "+variable);


        try {
            ExpressionChecker.checkExpression(fromLimit);
        } catch (Exception e) {
            throw new Exception("From field is malformed");
        }

        try {
            ExpressionChecker.checkExpression(toLimit);
        } catch (Exception e) {
            throw new Exception("to field is malformed");
        }

        IntegrateItem integrateItem = new IntegrateItem(expression,fromLimit,toLimit);
        return integrateItem.getInput();
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
