package com.abacus.android.equation.parser;

import android.content.Context;

import com.abacus.android.equation.evaluator.exceptions.EmptyInputExpressionException;

/**
 * Created by sunil28 on 11/7/18.
 */

public class InputClassifier {

    Context mContext;
    public static final String INTEGRATION_OPERATOR="\\int";
    public static final String DERIVATIVE_OPERATOR="\\frac { d } { d x }";
    public static final String SIMULTANEOUS_EQUATION_OPERATOR="\\\\";
    public static final String QUADRATIC_OPERATOR="^";
    public static  boolean EQUATION_OPERATOR=false;

    private String operation;

    public InputClassifier(Context cntx){
        this.mContext=cntx;
    }

    public String executeClassifier(String input)  {

        if(input.isEmpty())
            throw new EmptyInputExpressionException(input);

        if(input.contains(INTEGRATION_OPERATOR)){
            IntegrationEvaluator evaluator = new IntegrationEvaluator();
            operation = "Integration";
            return  evaluator.getSymjaInput(input);
        }


        if(input.contains(DERIVATIVE_OPERATOR)){
            DerivativeEvaluator evaluator = new DerivativeEvaluator();
            operation = "Derivation";

            return  evaluator.getSymjaInput(input);
        }

        if(input.contains(SIMULTANEOUS_EQUATION_OPERATOR)){
            SimultaneousEquationEvaluator evaluator = new SimultaneousEquationEvaluator();
            operation = "Simultaneous";
            return  evaluator.getSymjaInput(input);
        }

        for(char c : input.toCharArray())
            if(Character.isLetter(c)){
                EQUATION_OPERATOR=true;
                break;
            }

        if(input.contains(QUADRATIC_OPERATOR ) && EQUATION_OPERATOR){
            QuadraticEquationEvaluator evaluator = new QuadraticEquationEvaluator();
            operation = "Quadratic";
            return  evaluator.getSymjaInput(input);
        }


        if(EQUATION_OPERATOR){
            SimpleExpressionEvaluator evaluator = new SimpleExpressionEvaluator();
            operation = "Simple";
            return  evaluator.getSymjaInput(input);
        }

        if(!EQUATION_OPERATOR) {
            GeneralSolver evaluator = new GeneralSolver();
            operation = "General";
            return evaluator.getSymjaInput(input);
        }

        try {
            throw new Exception("This type of problem is not supported !!!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getOperation() {
        return operation;
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
