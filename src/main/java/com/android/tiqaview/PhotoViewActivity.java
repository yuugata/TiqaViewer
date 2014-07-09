package com.android.tiqaview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.tiqaview.tiqav.Item;

import java.util.List;

public class PhotoViewActivity extends ActionBarActivity {

    private static final String TAG = "PhotoViewActivity";
    public static final String ITEMS = "items";
    public static final String SHOW_INDEX = "show_index";

    public static final String INTENT_KEY_ENABLE_RETURN = "enable_return";
    private boolean enableReturnMode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        Intent intent = getIntent();
        handleIntent(intent);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final int menuResource = enableReturnMode ? R.menu.photo_view_return : R.menu.photo_view;
        getMenuInflater().inflate(menuResource, menu);
        //getMenuInflater().inflate(R.menu.photo_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "on options item selected " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        List<Item> items = (List<Item>) intent.getSerializableExtra(ITEMS);
        int index = intent.getIntExtra(SHOW_INDEX, 0);
        if (items == null || items.size() < 1) {
            Log.e(TAG, "Items are null or empty.");
            finish();
        }
        createPhotoViewPager(items, index);
        enableReturnMode = intent.getBooleanExtra(INTENT_KEY_ENABLE_RETURN, false);
    }

    private void createPhotoViewPager(List<Item> items, int homeIndex) {
        FragmentManager fm = getSupportFragmentManager();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final PhotoViewAdapter adapter = new PhotoViewAdapter(fm);
        adapter.setItems(items);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(homeIndex);
    }
}
