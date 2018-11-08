package com.abacus.android.parser;

import com.abacus.android.evaluator.exceptions.EmptyInputExpressionException;
import com.abacus.android.tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.abacus.android.parser.CalculusExample.main;


/**
 * Created by sunil28 on 11/5/18.
 */

public class SimultaneousEquationEvaluator {

    public Tokenizer mTokenizer = new Tokenizer();

    public SimultaneousEquationEvaluator(){

    }

    public String getSymjaInput(String expr){
        if(!expr.isEmpty()){
            ArrayList<String> exprList = filterLatex(expr);
            return main(createExpression(exprList));


        } else {
            throw new EmptyInputExpressionException(expr);
        }
    }
    public ArrayList<String> filterLatex(String latex){

        ArrayList<String> exprList = new ArrayList<String>();

        if(latex.isEmpty())
            throw new EmptyInputExpressionException(latex);


        String temp =latex;
        //String temp = "\\begin{array} { l } { x + y + z = 0 } \\\\ { x - y - z = 0 }  \\\\ { x + y - z = 0 } \\end{array}";
        //String temp="\\begin{array} { l } { 2 x + 3 } \\\\ { 2 y + 3 - 4 = 0 } \\end{array}";
        String start="\\left. \\begin{array} { l } ";
        String end=" \\end{array} \\right";

        //Removing the start and end of array
        temp = temp.substring(start.length(),temp.length()-end.length());

        //Splitting based on "//"
        System.out.println("==========="+temp+"===========");
        String [] words = temp.split("\\\\\\\\");

       for(String expr : words){
           if(expr.contains("{"))
               expr=expr.replaceAll("\\{","");

           if(expr.contains("}"))
               expr=expr.replaceAll("\\}","");

           if(expr.contains(" "))
               expr=expr.replaceAll(" ","");

           if(!expr.trim().equals(""))
               exprList.add(expr.trim());
       }
        return exprList;
    }

    public String createExpression(ArrayList<String> exprList) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (String expr : exprList) {
            expr = mTokenizer.getNormalExpression(expr);
            expr = replaceEqualSymbol(expr);
            if (!expr.isEmpty()) arrayList.add(expr);
        }

        StringBuilder equation = new StringBuilder();
        equation.append("Solve({");
        for (int i = 0; i < arrayList.size(); i++) {
            String s = arrayList.get(i);
            s = replaceEqualSymbol(s);
            if (i != arrayList.size() - 1) {
                equation.append(s);
                equation.append(",");
            } else {
                equation.append(s);
            }
        }

        String variables = getVariables(exprList);
        equation.append("}");
        equation.append(",");
        equation.append("{").append(variables).append("}");
        equation.append(")");
        return equation.toString();
    }

    public String replaceEqualSymbol(String s) {
        if (!s.contains("=")) s = s + "==0";
        if (!s.contains("==")) s = s.replace("=", "==");
        while (s.contains("===")) s = s.replace("===", "==");
        return s;
    }

    public String getVariables(ArrayList<String> exprList){
        StringBuilder result = new StringBuilder();
        Set<String> set = new HashSet<String>();

        for(String var :exprList){
            for(char c : var.toCharArray())
                if(Character.isLetter(c)){
                set.add(Character.toString(c));
            }
        }

        for(String s : set){
            result.append(s);
            result.append(",");
        }
        if(result.length()>0)
            result.deleteCharAt(result.length()-1);

        return result.toString();
    }
}
