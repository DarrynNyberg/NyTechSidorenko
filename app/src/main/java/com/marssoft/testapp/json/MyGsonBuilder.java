package com.marssoft.testapp.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by alexey on 24-Jan-16.
 */
public class MyGsonBuilder {
    private static final String TAG = MyGsonBuilder.class.getSimpleName();
    private static Gson instance;

    public static synchronized Gson getInstance() {
        if (instance == null) {
            instance = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) // will use with our server
                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
//                    .registerTypeAdapter(Location.class, new LocationTypeAdapter())
                    .create();
        }
        return instance;
    }
}
