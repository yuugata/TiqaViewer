package com.android.tiqaview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.tiqaview.tiqav.TiqavItem;

import java.util.List;

/**
 *
 */
public class SearchAdapter extends ArrayAdapter<TiqavItem>{

    private LayoutInflater mInflater;
    private int mLayoutResource;

    public SearchAdapter(Context context, int resource, List<TiqavItem> objects) {
        super(context, resource,objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        SearchItemView view = (SearchItemView) convertView;
        ViewHolder holder;
        if(convertView == null){
            //view = (SearchItemView) mInflater.inflate(mLayoutResource,null);
            convertView = mInflater.inflate(mLayoutResource,null);
        holder = new ViewHolder();
            holder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        TiqavItem item = getItem(position);
        holder.descriptionView.setText(item.getSource_url());

        return convertView;
    }

    private static class ViewHolder {
        TextView descriptionView;
    }
}
