package com.cronycle.client.adapters;

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
	
	private CronycleCollection[] collections;

    public CollectionsAdapter(Context c, CronycleCollection[] collections) {
        this.mContext = c;
        this.collections = collections;
    }

    public int getCount() {
        return collections.length;
    }

    public CronycleCollection getItem(int position) {
        return collections[position];
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
        } else {
        	item = (View) convertView;
        }
        
        item.setBackgroundColor(collection.settings.getColor());
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(collection.name);
        
        TextView stats = (TextView) item.findViewById(R.id.stats);
        stats.setText(String.format("%s entries", collection.getTotalLinksCount()));
        
        ImageView cover = (ImageView) item.findViewById(R.id.cover);
        
        if (convertView == null) {
        	 title.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
        	 stats.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
        }
        
        if (collection.cover_asset != null) {
        	Picasso.with(mContext).load(collection.cover_asset.getSmallOrDefaultAsset()).into(cover);
        }
        
        return item;
    }
}