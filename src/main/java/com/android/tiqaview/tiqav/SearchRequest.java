package com.android.tiqaview.tiqav;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 *
 */
public class SearchRequest extends JsonArrayRequest {

    SearchRequest(String query, final Response.Listener<ArrayList<Item>> listener, Response.ErrorListener errorListener) {

        super("http://api.tiqav.com/search.json?q=" + query, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                listener.onResponse(TiqavObjectFactory.createItems(jsonArray));
            }
        }, errorListener);
    }

}
