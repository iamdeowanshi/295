package com.abacus.android.parser;

import android.content.Context;

import com.abacus.android.evaluator.exceptions.EmptyInputExpressionException;

import java.util.ArrayList;

/**
 * Created by sunil28 on 11/7/18.
 */

public class InputClassifier {

    Context mContext;
    public static final String INTEGRATION_OPERATOR="\\int";
    public static final String DERIVATIVE_OPERATOR="\\frac { d } { d x }";
    public static final String SIMULTANEOUS_EQUATION_OPERATOR="\\\\";
    public static final String QUADRATIC_OPERATOR="^";
    public static final String EQUATION_OPERATOR="x";


    public InputClassifier(Context cntx){
        this.mContext=cntx;
    }

    public String executeClassifier(String input)  {

        if(input.isEmpty())
            throw new EmptyInputExpressionException(input);

        if(input.contains(INTEGRATION_OPERATOR)){
            IntegrationEvaluator evaluator = new IntegrationEvaluator();
            return  evaluator.getSymjaInput(input);
        }


        if(input.contains(DERIVATIVE_OPERATOR)){
            DerivativeEvaluator evaluator = new DerivativeEvaluator();
            return  evaluator.getSymjaInput(input);
        }

        if(input.contains(SIMULTANEOUS_EQUATION_OPERATOR)){
            SimultaneousEquationEvaluator evaluator = new SimultaneousEquationEvaluator();
            return  evaluator.getSymjaInput(input);
        }

        if(input.contains(QUADRATIC_OPERATOR)){
            QuadraticEquationEvaluator evaluator = new QuadraticEquationEvaluator();
            return  evaluator.getSymjaInput(input);
        }

        if(input.contains(EQUATION_OPERATOR)){
            SimpleExpressionEvaluator evaluator = new SimpleExpressionEvaluator();
            return  evaluator.getSymjaInput(input);
        }

        try {
            throw new Exception("This type of problem is not supported !!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

   /* public Command<ArrayList<String>, String> getCommand() {
        return new Command<ArrayList<String>, String>() {
            @Override
            public ArrayList<String> execute(String input) {
                EvaluateConfig config = EvaluateConfig.loadFromSetting(mContext);
                String fraction = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        config.setEvalMode(EvaluateConfig.FRACTION));

                String decimal = MathEvaluator.getInstance().evaluateWithResultAsTex(input,
                        config.setEvalMode(EvaluateConfig.DECIMAL));

                ArrayList<String> result = new ArrayList<>();
                result.add(fraction);
                result.add(decimal);
                return result;
            }
        };
    }*/
}
