package com.abacus.poc295.service;


import com.abacus.poc295.Config;

import java.util.Iterator;
import java.util.List;

/**
 * Created by aaditya on 10/21/17.
 */


public class Token {

    private static Token token;
    private List<String> tokens = Config.KEYS;
    private Iterator<String> it;

    private Token() {
        it = tokens.iterator();
        if (token != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static Token getInstance(){
        if (token == null){ //if there is no instance available... create new one
            token = new Token();
        }

        return token;
    }

    public String getToken() {
        if (!it.hasNext()) {
            it = tokens.iterator();
        }

        String token = it.next();

        return token;
    }
}
