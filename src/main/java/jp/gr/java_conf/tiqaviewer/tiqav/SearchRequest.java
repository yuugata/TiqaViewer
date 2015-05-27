package jp.gr.java_conf.tiqaviewer.tiqav;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class SearchRequest extends JsonArrayRequest {

    SearchRequest(String query, final Response.Listener<ArrayList<Item>> listener, Response.ErrorListener errorListener) {

        super("http://api.tiqav.com/search.json?q=" + query, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                listener.onResponse(createItems(jsonArray));
            }
        }, errorListener);
    }

    static ArrayList<Item> createItems(JSONArray jsonArray) {
        int length = jsonArray.length();

        ArrayList<Item> items = new ArrayList<Item>();

        if (length <= 0) {
            return null;
        }

        for (int i = 0; i < length; i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                Item item = Item.create(obj);
                items.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public static class Builder {

        private String query;
        Response.Listener<ArrayList<Item>> listener;
        Response.ErrorListener errorListener;

        public Builder(){
        }

        public Builder setListener(Response.Listener<ArrayList<Item>> listener){
            this.listener = listener;
            return this;
        }

        public Builder setErrorListener(Response.ErrorListener errorListener){
            this.errorListener = errorListener;
            return this;
        }

        public Builder setQuery(String query){
            this.query = query;
            return this;
        }

        public SearchRequest build(){
            return new SearchRequest(this.query,this.listener,this.errorListener);
        }
    }

}
