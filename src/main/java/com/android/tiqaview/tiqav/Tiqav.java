package com.android.tiqaview.tiqav;

import com.android.volley.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Tiqav
 */
public class Tiqav {

    public static SearchRequest createSearchRequest(String query, Response.Listener<ArrayList<Item>> listener, Response.ErrorListener errorListener) {

        return new SearchRequest(encode(query), listener, errorListener);
    }

    private static String encode(String q) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(q, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoded;
    }

}
