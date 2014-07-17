package com.cronycle.client.adapters;

import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
import com.cronycle.client.Libs.CronycleCollection;
import com.squareup.picasso.Picasso;

public class CollectionsAdapter extends BaseDynamicGridAdapter {
	
	private Context mContext;

    public CollectionsAdapter(Context context, List<?> items, int columnCount) {
        super(context, items, columnCount);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View item;
        
        CronycleCollection collection = (CronycleCollection) getItem(position);
        
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
        } else {
        	cover.setImageResource(android.R.color.transparent);
        }
        
        return item;
    }
}