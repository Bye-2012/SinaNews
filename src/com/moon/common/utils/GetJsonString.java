package com.moon.common.utils;

import android.util.Log;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.moon.app.AppCtx;

/**
 * Created by IntelliJ IDEA
 * User: Moon
 * Date:2015/4/21
 */
public class GetJsonString {
    private static RequestQueue requestQueue = AppCtx.getInstance().getRequestQueue();

    public static void getJsonString(String url,final StringListener listener){
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.stringListener(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GetJsonString","error..1111");
            }
        });
        requestQueue.add(request);
    }

    public interface StringListener{
        void stringListener(String jsonStr);
    }
}