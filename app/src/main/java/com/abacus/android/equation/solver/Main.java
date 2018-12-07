package com.abacus.android.equation.solver;

public class Main {


    public static void main(String[] args) {
	// write your code here
        String input = "(( -3 + 5) * 2^4 +10)";

        ResultObj ans = new RPNCalc(input).eval();
        for(String step : ans.getSteps()){
            System.out.println(step);
        }

        System.out.println(ans.getAnswer());
    }
}
