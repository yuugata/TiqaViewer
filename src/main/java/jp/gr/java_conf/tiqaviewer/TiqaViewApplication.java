package jp.gr.java_conf.tiqaviewer;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * TiqaView Application
 */
public class TiqaViewApplication extends Application {
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.start();
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
