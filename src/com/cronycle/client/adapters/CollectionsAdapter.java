package com.cronycle.client.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
import com.cronycle.client.Libs.CronycleCollection;
import com.squareup.picasso.Picasso;

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

    public CronycleCollection getItem(int position) {
        return collections.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View item;
        
        CronycleCollection collection = getItem(position);
        
        if (convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(mContext);
        	
        	item = inflater.inflate(R.layout.grid_collection, parent, false);
            item.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 400));
        } else {
        	item = (View) convertView;
        }
        
        item.setBackgroundColor(collection.settings.getColor());
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(collection.name);
        
        ImageView cover = (ImageView) item.findViewById(R.id.cover);
        
        if (convertView == null) {
        	 title.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaBold);
        }
        
        if (collection.cover_asset != null) {
        	Picasso.with(mContext).load(collection.cover_asset.getSmallOrDefaultAsset()).into(cover);
        }
        
        return item;
    }
}