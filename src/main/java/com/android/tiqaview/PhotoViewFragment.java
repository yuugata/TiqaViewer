package com.android.tiqaview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.tiqaview.tiqav.Item;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *
 */
public class PhotoViewFragment extends Fragment implements Response.Listener<Bitmap>, Response.ErrorListener {
    private static final String TAG = "PhotoViewFragment";
    public static final String IMAGE_URL = "image_url";

    private RequestQueue mRequestQueue;
    private PhotoViewAttacher mAttacher;
    private ImageView mImageView = null;

    public PhotoViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = ((TiqaViewApplication) getActivity().getApplication()).getRequestQueue();
        Bundle args = getArguments();
        if (args != null) {
            String url = args.getString(IMAGE_URL);
            loadImage(url);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        mImageView = (ImageView) rootView.findViewById(R.id.imageView);

        return rootView;
    }

    private void loadImage(String url) {
        Log.d(TAG, url);
        ImageRequest request;
        request = new ImageRequest(url, this, 0, 0, Bitmap.Config.ARGB_8888, this);
        mRequestQueue.add(request);
    }

    @Override
    public void onResponse(Bitmap bitmap) {
        Log.d(TAG, "--- on Response ---");
        if (mImageView == null)
            return;
        mImageView.setImageBitmap(bitmap);
        mAttacher = new PhotoViewAttacher(mImageView);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAttacher != null)
            mAttacher.cleanup();
    }
}