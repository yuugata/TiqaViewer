package com.android.tiqaview.tiqav;

import com.android.volley.Response;

import java.util.List;

/**
 * Tiqav
 */
public class Tiqav {

    public static SearchRequest createSearchRequest(String query,Response.Listener<List<Item>> listener, Response.ErrorListener errorListener){
        return new SearchRequest(query,listener,errorListener);
    }

}
