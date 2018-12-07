package com.abacus.android.equation.solver;

import java.util.*;

enum  Associativity{
    LEFT_ASSOCIATIVITY,
    RIGHT_ASSOCIATIVITY
}

enum Precedence{
    LOW,
    MEDIUM,
    HIGH
}

class operatorAttributes{

    private Associativity associativity_val;
    private  Precedence precedence_val;

    public operatorAttributes(Associativity associativity_val, Precedence precedence_val) {
        this.associativity_val = associativity_val;
        this.precedence_val = precedence_val;
    }

    public Associativity getAssociativity_val() {
        return associativity_val;
    }

    public void setAssociativity_val(Associativity associativity_val) {
        this.associativity_val = associativity_val;
    }

    public Precedence getPrecedence_val() {
        return precedence_val;
    }

    public void setPrecedence_val(Precedence precedence_val) {
        this.precedence_val = precedence_val;
    }
}


public class ReversePolishNotation {

    private static final Map<String , operatorAttributes> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", new operatorAttributes(Associativity.LEFT_ASSOCIATIVITY, Precedence.LOW) );
        OPERATORS.put("-", new operatorAttributes(Associativity.LEFT_ASSOCIATIVITY, Precedence.LOW) );
        OPERATORS.put("*", new operatorAttributes(Associativity.LEFT_ASSOCIATIVITY, Precedence.MEDIUM) );
        OPERATORS.put("/", new operatorAttributes(Associativity.LEFT_ASSOCIATIVITY, Precedence.MEDIUM) );
        OPERATORS.put("%", new operatorAttributes(Associativity.LEFT_ASSOCIATIVITY, Precedence.MEDIUM) );
        OPERATORS.put("^", new operatorAttributes(Associativity.RIGHT_ASSOCIATIVITY, Precedence.HIGH) );
        OPERATORS.put("!", new operatorAttributes(Associativity.RIGHT_ASSOCIATIVITY, Precedence.HIGH));
    }

    public static Map<String, operatorAttributes> getOPERATORS() {
        return OPERATORS;
    }

    private static boolean isOperator(String str){
        return OPERATORS.containsKey(str);
    }

    private static boolean isAssociative(String token, Associativity type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        return  type.equals(OPERATORS.get(token).getAssociativity_val()) ? true : false;
    }

    private static final int cmpPrecedence(String token1, String token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1).getPrecedence_val().compareTo(OPERATORS.get(token2).getPrecedence_val());
    }

    public static String[] infixToRPN(String input) {
        String [] inputTokens = parse(input);
        ArrayList<String> out = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : inputTokens) {
            if(token.trim().isEmpty())continue;
            if (isOperator(token)) {
                while (!stack.empty() && isOperator(stack.peek())) {
                    if ((isAssociative(token, Associativity.LEFT_ASSOCIATIVITY) && cmpPrecedence(
                            token, stack.peek()) <= 0)
                            || (isAssociative(token, Associativity.RIGHT_ASSOCIATIVITY) && cmpPrecedence(
                            token, stack.peek()) < 0)) {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } else {
                out.add(token);
            }
        }
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        return out.stream().toArray(String[]::new);
    }

    static String[] parse1(String str) {
        return str.split("(?<=[-+*/%^()!])|(?=[-+*/%^()!])");
    }

    private static String[] parse(String input){
        StringBuilder strBld =  new StringBuilder();
        for(Character c : input.toCharArray()){
            if(c == ' ') continue;
            strBld.append(c);
        }
        String processed = strBld.toString();
        strBld.setLength(0);
        List<String> tokens = new ArrayList<>();
        for(int i = 0; i < processed.length() ; i++){
            char c = processed.charAt(i);
            if(c == '-'){
                StringBuilder str1 = new StringBuilder();
                str1.append(c);
                int next = i+1;
                while(next < processed.length() && (Character.isDigit(processed.charAt(next)) || processed.charAt(next) == '.')){
                    str1.append(processed.charAt(next));
                    next++;
                }
                tokens.add(str1.toString());
                str1.setLength(0);
                i = next - 1;
            }else if(c == '(' || c == ')' || c == '/' || c == '*' || c == '+' || c== '^' || c=='%' || c=='!'){
                tokens.add(String.valueOf(c));
            }else{
                while(i < processed.length() && (Character.isDigit(processed.charAt(i)) || processed.charAt(i) == '.')){
                    strBld.append(processed.charAt(i));
                    i++;
                }
                i--;
                tokens.add(strBld.toString());
                strBld.setLength(0);
            }
        }

        for(String str : tokens){
            System.out.println(str);
        }

        return tokens.stream().toArray(String[]::new);
    }
}
