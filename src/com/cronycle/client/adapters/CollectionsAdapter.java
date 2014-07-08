package com.cronycle.client.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.cronycle.client.Libs.CronycleCollection;

public class CollectionsAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private List<CronycleCollection> collections;

    public CollectionsAdapter(Context c, List<CronycleCollection> collections) {
        this.mContext = c;
        this.collections = collections;
    }

    public int getCount() {
        return collections.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        
        CronycleCollection collection = collections.get(position);
        
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 400));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setBackgroundColor(collection.settings.getColor());
        } else {
            imageView = (ImageView) convertView;
        }
        
        if (collection.cover_asset != null) {
        	
        }
        
        return imageView;
    }
}