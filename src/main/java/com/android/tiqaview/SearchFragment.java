package com.android.tiqaview;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.tiqaview.tiqav.Item;
import com.android.tiqaview.tiqav.SearchRequest;
import com.android.tiqaview.tiqav.Tiqav;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 検索結果表示用
 */
public class SearchFragment extends Fragment implements Response.Listener<ArrayList<Item>>, Response.ErrorListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String SEARCH_QUERY = "search_query";
    private static final String TAG = "SearchFragment";

    public static final String INTENT_KEY_ENABLE_RETURN = "enable_return";
    private boolean enableReturnMode = false;
    private static final int REQUEST_RETURN_IMAGE = 1;

    private RequestQueue mRequestQueue;
    private GridView mGridView;
    private ProgressBar mProgressBar = null ;
    private ArrayList<Item> mItems;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = ((TiqaViewApplication) getActivity().getApplication()).getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.actionbar_progress);
        Bundle args = getArguments();
        if (args != null) {
            String query = args.getString(SEARCH_QUERY);
            enableReturnMode = args.getBoolean(INTENT_KEY_ENABLE_RETURN, false);
            startSearch(query);
            Log.d("main", "start search:" + query);
        }

        return rootView;
    }// */

    private void startSearch(String query) {
        Log.d(TAG, query);
        SearchRequest request = Tiqav.createSearchRequest(query, this, this);
        request.setTag(query);
        mRequestQueue.add(request);
    }

    @Override
    public void onResponse(ArrayList<Item> items) {
        SearchAdapter adapter = new SearchAdapter(getActivity(), R.layout.search_item, items);
        adapter.setImageLoader(new ImageLoader(mRequestQueue,
                new LruImageCache(((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass())));
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        mItems = items;

        if(mProgressBar != null){
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), "Error response : " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(TAG, volleyError.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        startPhotoViewActivity(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){
            return;
        }

        if(resultCode == getActivity().RESULT_OK){
            switch (requestCode){
                case REQUEST_RETURN_IMAGE :
                    getActivity().setResult(resultCode, data);
                    getActivity().finish();
                    break;
            }
        }
    }

    private void startPhotoViewActivity(int position) {
        Intent intent = new Intent(getActivity().getApplicationContext(), PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.ITEMS, mItems);
        intent.putExtra(PhotoViewActivity.SHOW_INDEX, position);
        intent.putExtra(PhotoViewActivity.INTENT_KEY_ENABLE_RETURN, enableReturnMode);
        startActivityForResult(intent, REQUEST_RETURN_IMAGE);
    }

}
