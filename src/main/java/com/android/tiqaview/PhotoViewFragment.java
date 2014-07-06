package com.android.tiqaview;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoView;

/**
 *
 */
public class PhotoViewFragment extends Fragment implements Response.Listener<Bitmap>, Response.ErrorListener {
    private static final String TAG = "PhotoViewFragment";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_TITLE = "image_title";

    private RequestQueue mRequestQueue;
    private PhotoView mPhotoView = null;
    private Bitmap mDownloadedImageBitmap = null;
    private String mImageTitle = null;

    public PhotoViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = ((TiqaViewApplication) getActivity().getApplication()).getRequestQueue();

        setHasOptionsMenu(true);
        //getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        mPhotoView = (PhotoView) rootView.findViewById(R.id.photoView);
        Bundle args = getArguments();
        if (args != null) {
            mImageTitle = args.getString(IMAGE_TITLE);
            String url = args.getString(IMAGE_URL);
            loadImage(url);
        }
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
        if (mPhotoView == null){
            Log.e(TAG,"null view");
            return;
        }
        if(!isAdded()){
            return ;
        }
        mDownloadedImageBitmap = bitmap;
        //mImageView.setImageBitmap(bitmap);
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mPhotoView.setImageDrawable(drawable);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.photo_view, menu);
        Log.d(TAG, "create option menu" + mImageTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBitmap(mDownloadedImageBitmap);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveBitmap(Bitmap b) {
        if (b == null)
            return;
        try {
            final File pubDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (pubDir == null) return;

            String fileName = mImageTitle;
            if (TextUtils.isEmpty(fileName)) {
                long utc = System.currentTimeMillis();
                fileName = Long.toString(utc);
            }
            fileName = fileName + ".jpg";

            FileOutputStream fos = null;
            fos = new FileOutputStream(new File(pubDir, fileName));

            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.close();

            Toast.makeText(getActivity(), getString(R.string.saved_file, pubDir.getPath() + fileName), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(getActivity(), R.string.failed_to_save, Toast.LENGTH_LONG).show();
        }

    }
}