package com.abacus.android.equation.solver;

import java.text.DecimalFormat;
import java.util.*;

public class RPNCalc {

    // Set of supported operators

    private List<String> steps;
    private String expression;
    private String[] rpn_expression;

    private static DecimalFormat df2 = new DecimalFormat(".##");

    public RPNCalc(String input){
        this.steps = new ArrayList<>();
        this.expression = input;
        this.rpn_expression = ReversePolishNotation.infixToRPN(this.expression);
        for(String str : rpn_expression)
            System.out.println(str);
    }

    private Double factorialSolver(Double num1){
        Double temp , ans;
        temp = num1;
        ans = 1.0;
        while(temp != 0){
            ans *= temp;
            temp -= 1.0;
        }
        return ans;
    }

    // Operation based on operator
    public Double operation(String op, Double e2, Double e1) {
        Double ans = null;
        String operation = null;
        switch (op){
            case "+":
                operation = "ADDITION";
                ans = e1 + e2;
                break;
            case "-":
                operation = "SUBTRACTION";
                ans = e1 - e2;
                break;
            case "*":
                operation = "MULTIPLICATION";
                ans = e1 * e2;
                break;
            case "/":
                operation = "DIVISION";
                ans = e1 / e2;
                break;
            case "^":
                operation = "POWER";
                ans = Math.pow(e1,e2);
                break;
            case "%":
                operation = "MODULO";
                ans = e1 % e2;
                break;
            case "!":
                operation = "FACTORIAL";
                ans = factorialSolver(e1);
                break;
            default:
                throw new IllegalArgumentException("Invalid Operator found..."+ op);
        }
        addSteps(operation, e1, e2, ans);
        return ans;
    }

    private void addSteps(String operation, Double operand1, Double operand2, Double res){
        StringBuilder str = new StringBuilder();
        StringBuilder str1 = new StringBuilder();
        switch (operation) {
            case "ADDITION":
                str.append("Add "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")) +" in "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" + "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "SUBTRACTION":
                str.append("Subtract "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")) +" from "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" - "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "MULTIPLICATION":
                str.append("Multiply "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")) +" with "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" * "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "DIVISION":
                str.append("Divide "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")) +" by "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" / "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "MODULO":
                str.append("Take modulo of "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")) +" by "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" % "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "POWER":
                str.append("Take power of "+ String.valueOf(getFormattedValue(df2.format(operand1)+"")) +" by "+ String.valueOf(getFormattedValue(df2.format(operand2)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+" ^ "+String.valueOf(getFormattedValue(df2.format(operand2)+"")) + " = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
            case "FACTORIAL":
                str.append("Calculate the factorial of "+String.valueOf(getFormattedValue(df2.format(operand1)+"")));
                str1.append(String.valueOf(getFormattedValue(df2.format(operand1)+""))+"! = " + String.valueOf(getFormattedValue(df2.format(res)+"")));
                break;
        }
        this.steps.add(str.toString());
        this.steps.add(str1.toString());

    }

    // Evaluate RPN expr (given as array of tokens)
    public ResultObj eval() {
        LinkedList<Double> stack = new LinkedList<>();
        try {
            for (String token : this.rpn_expression) {
                if (ReversePolishNotation.getOPERATORS().containsKey(token)) {
                    if(!token.equals("!"))
                        stack.push(operation(token, stack.pop(), stack.pop()));
                    else{
                        stack.push(operation(token, null, stack.pop()));
                    }
                } else {
                    stack.push(Double.parseDouble(token));
                }
            }
            return new ResultObj(this.steps, stack.pop(), "OK");
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            return new ResultObj(null, null, "ERROR");
        }
    }

    public Object getFormattedValue(String val){
            String [] values = val.split("\\.");
            int curr = Integer.valueOf(values[1])*1;
            if(curr==0)
                return Integer.valueOf(values[0]);

            return val;


    }

}
