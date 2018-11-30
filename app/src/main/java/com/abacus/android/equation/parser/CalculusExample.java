package com.abacus.android.equation.parser;

/**
 * Created by sunil28 on 11/5/18.
 */


import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

public class CalculusExample {
    public static String main(String expr) {
        IExpr result=null;
        try {
            // don't distinguish between lower- and uppercase identifiers
            Config.PARSER_USE_LOWERCASE_SYMBOLS = true;

            //ExprEvaluator util = new ExprEvaluator(false, 100);
            //result = util.evaluate(expr);

            IExpr iExpr = EvalEngine.get().parse(expr);
            result = EvalEngine.get().evaluate(iExpr);

            System.out.println(result.toString());



        } catch (SyntaxError e) {
            // catch Symja parser errors here
            System.out.println(e.getMessage());
        } catch (MathException me) {
            // catch Symja math errors here
            System.out.println(me.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}