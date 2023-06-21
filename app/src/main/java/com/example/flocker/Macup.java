package com.example.flocker;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Macup extends StringRequest {
    final static private String URL = "http://facelocker.dothome.co.kr/MACAddr_up.php";
    private Map<String,String>Params;

    public Macup(String id, String mac, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        Params = new HashMap<>();
        Params.put("user_id", id);
        Params.put("data", mac);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return Params;
    }
}
