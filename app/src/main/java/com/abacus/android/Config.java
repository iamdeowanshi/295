package com.abacus.android;

import android.content.pm.ActivityInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aaditya on 10/20/17.
 */

public class Config {

    //--------------------------------------------------------------------------------
    // App general configurations
    //--------------------------------------------------------------------------------
    public static final boolean DEBUG = true;

    public static final int ORIENTATION_PORTRAIT    = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    public static final int ORIENTATION_LANDSCAPE   = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    public static final int ORIENTATION_SENSOR      = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
    public static final int ORIENTATION_DEFAULT     = ORIENTATION_PORTRAIT;

    //--------------------------------------------------------------------------------
    // API related constants/configurations - used in ApiModule
    //--------------------------------------------------------------------------------
    public static final String API_BASE_URL_PRODUCTION = "https://api.mathpix.com/v3/";
    public static final String WORD_PROBLEM = "http://34.220.72.152:5000";

    public static final String WORD_PROBLEM_FEEDBACK = "http://34.220.72.152:5001";

    public static final String TIMEZONE_URL = "http://api.timezonedb.com/v2/";


    // Active base url
    public static final String WORD_PROBLEM_URL = WORD_PROBLEM;

    public static final String LATEX_URL = API_BASE_URL_PRODUCTION;
    public static final String LOGGING_URL = "http://34.220.72.152:8081";


    // Common http headers required to be added by interceptor
    public static final Map<String, String> API_HEADERS = new HashMap<String, String>() {{
        put("User-Agent", "Weather-App");
        put("Content-Type", "application/json");
    }};

    // Key
    public static final String KEY_ = "91221591f3415b3ea83e";
    public static final String KEY_TIMEZONE = "AZB2TC5HOU68";
    public static final String FORMAT_TIMEZONE = "json";
    public static final String BY_TIMEZONE = "position";


    public static final String APP_ID = "akhilesh_deowanshi_hotmail_com";


    public static final List<String> KEYS = Arrays.asList(
            "91221591f3415b3ea83e",
            "04e355de5c5e2857a452");

}
