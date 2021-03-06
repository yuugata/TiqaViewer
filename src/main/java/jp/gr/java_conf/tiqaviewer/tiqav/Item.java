package jp.gr.java_conf.tiqaviewer.tiqav;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Tiqavの検索結果の要素
 */
public class Item implements Serializable {

    private String id;
    private String ext;
    private int height;
    private int width;
    private String source_url;

    private Item() {
    }

    public String getId() {
        return id;
    }

    public String getExt() {
        return ext;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getSourceUrl() {
        return source_url;
    }

    public String getThumbnailUrl() {
        return "http://img.tiqav.com/" + id + ".th.jpg";
    }

    public String getOriginalUrl() {
        return "http://img.tiqav.com/" + id + "." + ext;
    }

    static Item create(JSONObject json) throws JSONException {
        Item item = new Item();

        item.id = json.getString("id");
        item.ext = json.getString("ext");
        item.height = json.getInt("height");
        item.width = json.getInt("width");
        item.source_url = json.getString("source_url");

        return item;
    }
}
