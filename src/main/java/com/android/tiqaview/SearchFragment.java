package com.android.tiqaview;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.tiqaview.tiqav.Item;
import com.android.tiqaview.tiqav.SearchRequest;
import com.android.tiqaview.tiqav.Tiqav;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 検索結果表示用
 */
public class SearchFragment extends Fragment implements Response.Listener<List<Item>>, Response.ErrorListener {

    public static final String SEARCH_QUERY = "search_query";
    private static final String TAG = "SearchFragment";

    private RequestQueue mRequestQueue;
    private ListView mResultListView;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = ((TiqaViewApplication)getActivity().getApplication()).getRequestQueue();
       // setHasOptionsMenu(true);
    }
///*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container,false);
        //mHelloTextView = (TextView) rootView.findViewById(R.id.hello_txt);
        mResultListView = (ListView) rootView.findViewById(R.id.listView);

        Bundle args = getArguments();
        if(args != null){
            String query = args.getString(SEARCH_QUERY);
          startSearch(query);
            Log.d("main", "start search:" + query);
        }

        return rootView;
    }// */

    private void startSearch(String query){
        Log.d(TAG,query);
        SearchRequest request = Tiqav.createSearchRequest(query, this,this);
        request.setTag(query);
       mRequestQueue.add(request);

    }

    @Override
    public void onResponse(List<Item> items) {

        SearchAdapter adapter = new SearchAdapter(getActivity(),R.layout.search_item,items);
        adapter.setImageLoader(new ImageLoader(mRequestQueue,
                new LruImageCache(((ActivityManager)getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass())));
        mResultListView.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(),volleyError.getMessage(),Toast.LENGTH_LONG).show();
    }
}
