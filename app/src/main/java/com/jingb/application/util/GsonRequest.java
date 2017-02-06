package com.jingb.application.util;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jingb.application.ninegag.imageload.model.GagDatagram;
import com.jingb.application.ninegag.imageload.model.GagdataJsonDeserializer;

import java.io.UnsupportedEncodingException;

public class GsonRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;

    private Gson mGson;

    private Class<T> mClass;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener, RetryPolicy retryPolicy) {
        super(method, url, errorListener);
        /**
         * GagdataJsonDeserializer custom deserializer,
         * it will be passed to the method in the future
         */
        mGson = new GsonBuilder().registerTypeHierarchyAdapter(
                GagDatagram.Media.class,
                new GagdataJsonDeserializer()
        ).create();
        mClass = clazz;
        mListener = listener;
        this.setRetryPolicy(retryPolicy);
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener,
                new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener, RetryPolicy retryPolicy) {
        this(Method.GET, url, clazz, listener, errorListener, retryPolicy);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

}