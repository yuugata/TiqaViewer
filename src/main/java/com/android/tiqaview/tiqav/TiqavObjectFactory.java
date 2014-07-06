package com.android.tiqaview.tiqav;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class TiqavObjectFactory {

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
}
