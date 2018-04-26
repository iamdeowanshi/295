package com.abacus.poc295;

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
    public static final String API_BASE_URL_PRODUCTION = "https://api.mathpix.com/v2/latex";
    public static final String API_BASE_URL_MOCK = "https://api.mathpix.com/v3/";

    public static final String WEATHER_ICON_URL = "http://openweathermap.org/img/w/";
    public static final String TIMEZONE_URL = "http://api.timezonedb.com/v2/";


    // Active base url
    public static final String API_BASE_URL = API_BASE_URL_MOCK;

    public static final String OPEN_WEATHER_URL = API_BASE_URL_MOCK;
    public static final String PLACES_URL = "https://maps.googleapis.com/maps/api/place/";


    // Common http headers required to be added by interceptor
    public static final Map<String, String> API_HEADERS = new HashMap<String, String>() {{
        put("User-Agent", "Weather-App");
        put("Content-Type", "application/json");
    }};

    // Key
    public static final String KEY_ = "04e355de5c5e2857a452";
    public static final String KEY_TIMEZONE = "AZB2TC5HOU68";
    public static final String FORMAT_TIMEZONE = "json";
    public static final String BY_TIMEZONE = "position";


    public static final String APP_ID = "akhilesh_deowanshi_hotmail_com";


    public static final List<String> KEYS = Arrays.asList(
            "b2c2f73ec36ac4a0a2ccd2107e34f54d",
            "7f225dfb054c4db8e8427a9e07753d77",
            "f3dae0e698039afa2098348484241311",
            "9eb1d8aed3bdc09c360faf7e61c01c51",
            "1cac6167e047af242e771ff28881f2ab",
            "fe8264bdaae5649e2ac309b1e5db924b");

}
