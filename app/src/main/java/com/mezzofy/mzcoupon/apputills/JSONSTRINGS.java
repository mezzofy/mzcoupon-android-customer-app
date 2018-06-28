package com.mezzofy.mzcoupon.apputills;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JSONSTRINGS {

    static InputStream is = null;
    static JSONObject jObj = null;

    static String json = "";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // constructor
    public JSONSTRINGS() {

    }
    public static String getJSONFromUrl(String urls) {

        // Making HTTP request
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor(CommonModule.getKey(),CommonModule.getSecretkey()))
                    .build();

            Request request = new Request.Builder()
                    .url(urls)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String responseBodyString = response.body().string();
                Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

                json = newResponse.body().string();

            } else {
                json = null;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            json = null;
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            json = null;
        }
        // return JSON String
        return json;

    }

    public static String getJSONFromUrl1(String urls) {

        // Making HTTP request
        try {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new BasicAuthInterceptor( "By3I7fX","x"))
                    .build();

            Request request = new Request.Builder()
                    .url(urls)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                String responseBodyString = response.body().string();
                Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

                json = newResponse.body().string();

            } else {
                json = null;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            json = null;
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            json = null;
        }
        // return JSON String
        return json;

    }



    public static JSONObject readJsonFromUrl(String urls) throws IOException, JSONException {

        try {
            String json = getJSONFromUrl(urls);
            jObj = new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;
    }

}