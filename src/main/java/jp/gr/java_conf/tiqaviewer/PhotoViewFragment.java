package jp.gr.java_conf.tiqaviewer;

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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 *
 */
public class PhotoViewFragment extends Fragment implements Response.Listener<Bitmap>, Response.ErrorListener, ShareActionProvider.OnShareTargetSelectedListener{
    private static final String TAG = "PhotoViewFragment";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_TITLE = "image_title";

    private RequestQueue mRequestQueue;
    private PhotoView mPhotoView = null;
    private Bitmap mDownloadedImageBitmap = null;
    private String mImageTitle = null;
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

        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v2) {
                toggleActionBarVisibility();
            }
        });

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

            // dummy File
            File file = createFileObj(false);
            if(file ==null) return;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");

            Uri uri = Uri.fromFile(file);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                File file = createFileObj(true);
                saveBitmap(file, mDownloadedImageBitmap);
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
        File file = createFileObj(true);
        if (!file.exists()) {
            saveBitmap(file, mDownloadedImageBitmap);
        }
        return false;
    }

    private void toggleActionBarVisibility(){
        final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();

        if( actionBar == null){
            return;
        }

        if(actionBar.isShowing()){
            actionBar.hide();
        }else{
            actionBar.show();
        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            Log.d(TAG, "set share intent");
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    private File createFileObj(boolean mkdir ) {
        final File pubDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (pubDir == null) return null;

        File saveDir = new File(pubDir,"TiqaViewer");

        if(mkdir){
            if(!saveDir.isDirectory() && !saveDir.mkdirs()){
                return null;
            }
        }

        String fileName;
        if (TextUtils.isEmpty(mImageTitle)) {
            long utc = System.currentTimeMillis();
            fileName = Long.toString(utc);
        }else{
            fileName = mImageTitle;
        }

        File file = new File(saveDir, fileName + ".jpg");
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
        File file = createFileObj(true);
        if(saveBitmap(file, bitmap) != null){
            intent.setData(Uri.fromFile(file));
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }
}