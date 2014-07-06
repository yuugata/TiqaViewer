package com.android.tiqaview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.tiqaview.tiqav.Item;

import java.util.List;

/**
 */
public class PhotoViewAdapter extends FragmentStatePagerAdapter {
    private List<Item> mItems = null;

    public PhotoViewAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setItems(List<Item> items){
        mItems = items;
    }

    @Override
    public Fragment getItem(int i) {
        Item item = mItems.get(i);

        Bundle bundle = new Bundle();
        bundle.putString(PhotoViewFragment.IMAGE_TITLE, item.getId());
        bundle.putString(PhotoViewFragment.IMAGE_URL, item.getOriginalUrl());

        PhotoViewFragment fragment = new PhotoViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
