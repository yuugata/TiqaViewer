package com.android.tiqaview.tiqav;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class TiqavObjectFactory {

   static List<Item> createItems(JSONArray jsonArray){
        int length = jsonArray.length();

        List<Item> items = new ArrayList<Item>();

        if(length <= 0){
            return null;
        }

        for(int i = 0; i < length; i++){
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
}
