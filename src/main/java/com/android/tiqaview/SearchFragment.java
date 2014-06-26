package com.android.tiqaview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.tiqaview.tiqav.TiqavItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 検索結果表示用
 */
public class SearchFragment extends Fragment {

    public static final String SEARCH_QUERY = "search_query";
    private static final String TAG = "SearchFragment";

    ListView mResultListView;
    //private TextView mHelloTextView;
    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
/*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            String query = args.getString(SEARCH_QUERY);
            startSearch(query);
            Log.d("main", "start search:" + query);
        }
    }
// */
    private void startSearch(String query){
        Log.d(TAG,query);

        List<TiqavItem> list = new ArrayList<TiqavItem>();
        TiqavItem item = new TiqavItem();
        item.source_url = query;
        list.add(item);
        //item.source_url="TiqaView";
        list.add(item);

        SearchAdapter adapter = new SearchAdapter(getActivity(),R.layout.search_item,list);
        //setListAdapter(adapter);
        mResultListView.setAdapter(adapter);
    }


}
