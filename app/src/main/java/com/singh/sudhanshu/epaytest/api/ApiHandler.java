package com.singh.sudhanshu.epaytest.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.singh.sudhanshu.epaytest.model.Balance;
import com.singh.sudhanshu.epaytest.model.Product;
import com.singh.sudhanshu.epaytest.ui.activity.AppCallback;
import com.singh.sudhanshu.epaytest.utils.Constants;
import com.singh.sudhanshu.epaytest.utils.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Sudhanshu on 6/4/2017.
 */

public class ApiHandler {

    private static final String TAG = ApiHandler.class.getSimpleName();

    // Instantiate the RequestQueue.

    public static void fetchTokenAndSaveIfNull(Context ctx, final AppCallback callback) {

        postRequest(ctx, APIs.LOGIN_URL, new AppCallback() {

            @Override
            public void onSuccess(Object data) {

                JSONObject object = ((JSONObject) data);
                try {
                    String token = object.getString("token");
                    Log.i(TAG, "saving token: " + token);

                    PreferenceUtil.getInstance().putStringValue("token", token);

                    callback.onSuccess(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object data) {
                callback.onFailure(null);
            }
        }, true, null);
    }

    /**
     * Fetches Get request
     *
     * @param ctx
     * @param url
     * @param callback
     */
    public static void getRequest(Context ctx, String url, final AppCallback callback, final boolean isLogin) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        // Request a string response from the provided URL.
        JSONObject params = new JSONObject();
        try {
            params.put("Accept", "application/json");
            params.put("Content-Type", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        url = paramsData.getEncodedParamsWithThisURL(url);

        Log.i(TAG, "url: " + url);
        Log.i(TAG, "params: " + params.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onResponse: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsMap = new HashMap<String, String>();

                if (!isLogin) {
                    appendHeader(paramsMap);
                }

                return paramsMap;
            }
        };

//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, params,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "onResponse: " + response);
//                        callback.onSuccess(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.i(TAG, "onResponse: " + error.getMessage());
//                callback.onFailure(error.getMessage());
//            }
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> paramsMap = new HashMap<String, String>();
//
//                if (!isLogin) {
//                    appendHeader(paramsMap);
//                }
//
//                return paramsMap;
//            }
//        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * Post request with params
     *
     * @param ctx
     * @param url
     * @param callback
     * @param isLogin
     */
    public static void postRequest(Context ctx, final String url, final AppCallback callback, final boolean isLogin,
                                   JSONObject params) {
        RequestQueue queue = Volley.newRequestQueue(ctx);

        // Request a string response from the provided URL.
        if (params == null) {
            params = new JSONObject();
        }

        try {
            params.put("Accept", "application/json");
            params.put("Content-Type", "application/json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "url: " + url);
        Log.i(TAG, "params: " + params.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "url: " + url);
                Log.i(TAG, "onResponse: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> paramsMap = new HashMap<String, String>();

                if (!isLogin) {
                    appendHeader(paramsMap);
                }

                return paramsMap;
            }
        };

// Add the request to the RequestQueue.
        queue.add(request);
    }

    /**
     * Appends the saved session header
     *
     * @param params
     */
    private static void appendHeader(Map<String, String> params) {
        String savedToken = PreferenceUtil.getInstance().getStringValue(Constants.PREF_TOKEN, null);
        params.put("Authorization", "Bearer " + savedToken);
    }

    /**
     * Fetches the user balance
     *
     * @param ctx
     * @param callback
     */
    public static void fetchBalance(Context ctx, final AppCallback callback) {

        ApiHandler.getRequest(ctx, APIs.BALANCE_URL, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                Gson gson = new Gson();
                Balance balance = gson.fromJson(((String) data), Balance.class);

                callback.onSuccess(balance);
            }

            @Override
            public void onFailure(Object data) {
                callback.onFailure(null);
            }
        }, false);
    }

    /**
     * Fetches the user transactions
     *
     * @param ctx
     * @param callback
     */
    public static void fetchTransactions(Context ctx, final AppCallback callback) {

        ApiHandler.getRequest(ctx, APIs.TRANSACTION_URL, new AppCallback() {
            @Override
            public void onSuccess(Object data) {

                Gson gson = new Gson();
                Type type = TypeToken.getParameterized(ArrayList.class, Product.class).getType();
                List<Product> transactionList = gson.fromJson((String) data, type);

                Log.d(TAG, "transaction: " + transactionList.size());
                callback.onSuccess(transactionList);
            }

            @Override
            public void onFailure(Object data) {

            }
        }, false);
    }
}
