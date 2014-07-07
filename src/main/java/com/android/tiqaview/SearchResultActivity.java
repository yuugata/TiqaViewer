package com.android.tiqaview;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class SearchResultActivity extends ActionBarActivity {

    private static final String TAG = "SearchResultActivity";
    private String mQuery = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        if (savedInstanceState == null) {

        }
        Intent intent = getIntent();
        handleIntent(intent);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "on new intent");
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_result, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery(mQuery,false);
        return super.onCreateOptionsMenu(menu);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            startSearch(query);
            mQuery = query;
        }
    }
    private void startSearch(String query) {
        Bundle bundle = new Bundle();
        bundle.putString(SearchFragment.SEARCH_QUERY, query);

        SearchFragment fragment;

        fragment = new SearchFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .add(R.id.container, fragment, "search");

        fragmentTransaction.commit();
    }
}