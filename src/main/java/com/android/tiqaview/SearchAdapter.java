package com.android.tiqaview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.tiqaview.tiqav.Item;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 *
 */
public class SearchAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private ImageLoader mImageLoader;

    public SearchAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        SearchItemView view = (SearchItemView) convertView;
        ViewHolder holder;
        if (convertView == null) {
            //view = (SearchItemView) mInflater.inflate(mLayoutResource,null);
            convertView = mInflater.inflate(mLayoutResource, null);
            holder = new ViewHolder();
            holder.imageView = (SquareImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.ImageContainer imageContainer = (ImageLoader.ImageContainer) holder.imageView.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        Item item = getItem(position);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher, R.drawable.ic_launcher);
        holder.imageView.setTag(mImageLoader.get(item.getThumbnailUrl(), listener));

        return convertView;
    }

    private static class ViewHolder {
        SquareImageView imageView;
    }

}
