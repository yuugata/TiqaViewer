package com.android.tiqaview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.tiqaview.tiqav.Item;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoView;

/**
 *
 */
public class PhotoViewFragment extends Fragment implements Response.Listener<Bitmap>, Response.ErrorListener, ShareActionProvider.OnShareTargetSelectedListener {
    private static final String TAG = "PhotoViewFragment";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_TITLE = "image_title";

    private RequestQueue mRequestQueue;
    private PhotoView mPhotoView = null;
    private Bitmap mDownloadedImageBitmap = null;
    private String mImageTitle = null;
    private File mImageFile = null;
    private ShareActionProvider mShareActionProvider = null;

    public PhotoViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = ((TiqaViewApplication) getActivity().getApplication()).getRequestQueue();

        setHasOptionsMenu(true);
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
        if (mPhotoView == null) {
            Log.e(TAG, "null view");
            return;
        }
        if (!isAdded()) {
            return;
        }
        mDownloadedImageBitmap = bitmap;
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mPhotoView.setImageDrawable(drawable);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.action_share);
        if (item != null) {
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            mShareActionProvider.setOnShareTargetSelectedListener(this);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            mImageFile = createFileObj();
            Uri uri = Uri.fromFile(mImageFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            setShareIntent(shareIntent);
        }

        Log.d(TAG, "create option menu :" + mImageTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "on option item select");
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBitmap(mImageFile, mDownloadedImageBitmap);
                return true;
            case R.id.action_share:
                break;
            case R.id.action_result:
                returnToActivity(mDownloadedImageBitmap);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onShareTargetSelected(ShareActionProvider shareActionProvider, Intent intent) {
        if (!mImageFile.exists()) {
            saveBitmap(mImageFile, mDownloadedImageBitmap);
        }
        return false;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            Log.d(TAG, "set share intent");
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private File createFileObj() {
        final File pubDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (pubDir == null) return null;

        String fileName = mImageTitle;
        if (TextUtils.isEmpty(fileName)) {
            long utc = System.currentTimeMillis();
            fileName = Long.toString(utc);
        }
        fileName = fileName + ".jpg";
        File file = new File(pubDir, fileName);
        return file;
    }

    private File saveBitmap( File file,Bitmap b) {
        if (file == null || b == null)
            return null;
        try {
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            Toast.makeText(getActivity(), getString(R.string.saved_file, file.getAbsolutePath()), Toast.LENGTH_LONG).show();
            return file;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.failed_to_save, Toast.LENGTH_LONG).show();
        }
        return null;
    }

    private void returnToActivity(Bitmap bitmap) {
        Intent intent = getActivity().getIntent();
        File file = createFileObj();
        if(saveBitmap(file, bitmap) != null){
            intent.setData(Uri.fromFile(file));
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }


}