package com.marssoft.testapp.json;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Date;

/**
 * Created by alexey on 24-Jan-16.
 */
public class DateTypeAdapter extends TypeAdapter<Date> {

    /*
        Format date from|to timestamp
    */

    private static final String TAG = DateTypeAdapter.class.getSimpleName();

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.value("null");
        } else {
            out.value(Math.ceil(value.getTime() / 1000));
        }
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            long milliseconds = in.nextLong();
            return new Date(milliseconds * 1000);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
