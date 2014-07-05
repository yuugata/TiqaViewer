package com.android.tiqaview;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class PhotoViewActivity extends ActionBarActivity {

    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_TITLE = "image_title";

    private String mImageTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoview);

        Intent intent = getIntent();
        handleIntent(intent);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_view, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
                 case android.R.id.home:
                     finish();
                     break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        mImageTitle = intent.getStringExtra(IMAGE_TITLE);
        String url = intent.getStringExtra(IMAGE_URL);
        if (TextUtils.isEmpty(url)) {
            finish();
            return;
        }
        loadImage(url);
    }

    private void loadImage(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(PhotoViewFragment.IMAGE_URL, url);
        bundle.putString(PhotoViewFragment.IMAGE_TITLE,mImageTitle);

        PhotoViewFragment fragment;

        fragment = new PhotoViewFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, null);

        fragmentTransaction.commit();
    }

}
