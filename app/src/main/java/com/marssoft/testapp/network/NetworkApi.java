package com.marssoft.testapp.network;

import android.os.AsyncTask;
import android.util.Log;


import com.google.gson.Gson;
import com.marssoft.testapp.json.MyGsonBuilder;
import com.marssoft.testapp.pojo.NetworkResponse;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by alexey on 23-Jan-16.
 */
public class NetworkApi {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String TAG = NetworkApi.class.getSimpleName();

    public interface NetworkCallback {
        void onError(String message);

        void onSuccess(Object result);
    }

    public static void sendUserDataByPost(final String name, final String email, final String phone, final NetworkCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {

                    RequestBody formBody = new FormBody.Builder()
                            .add("name", name)
                            .add("phone", phone)
                            .add("email", email)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://dev-nytechleads.pantheon.io/api/test")
                            .post(formBody)
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseJson = response.body().string();
                    Gson gson = MyGsonBuilder.getInstance();
                    NetworkResponse answer = gson.fromJson(responseJson, NetworkResponse.class);
                    if (answer == null){
                        if (callback != null) {
                            callback.onError(null);
                        }
                    } else {
                        if (callback != null) {
                            callback.onSuccess(answer);
                        }
                    }

                } catch (IOException e) {
                    String errMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    Log.e(TAG, errMsg, e);
                    if (callback != null) {
                        callback.onError(errMsg);
                    }

                }
                return null;
            }
        }.execute((Void) null);
    }

    public static void sendUserDataByGet(final String name, final String email, final String phone, final NetworkCallback callback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                try {

                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("http")
                            .host("dev-nytechleads.pantheon.io")
                            .addPathSegment("api/test")
                            .addEncodedQueryParameter("name", name)
                            .addEncodedQueryParameter("phone", phone)
                            .addEncodedQueryParameter("email", email)
                            .build();

                    Request request = new Request.Builder()
                            .url(url.url())
                            .build();

                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    if (callback != null) {
                        callback.onSuccess(response.body().string());
                    }
                } catch (IOException e) {
                    String errMsg = e.getMessage() != null ? e.getMessage() : e.toString();
                    Log.e(TAG, errMsg, e);
                    if (callback != null) {
                        callback.onError(errMsg);
                    }

                }
                return null;
            }
        }.execute((Void) null);
    }

}
