package com.cronycle.client.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
import com.cronycle.client.Libs.CronycleCollection;
import com.squareup.picasso.Picasso;

public class PublicCollectionsAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private ArrayList<CronycleCollection> collections;

    public PublicCollectionsAdapter(Context c, ArrayList<CronycleCollection> collections) {
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
        	item = inflater.inflate(R.layout.public_collection, parent, false);
        } else {
        	item = (View) convertView;
        }
        
        item.setBackgroundColor(collection.settings.getColor());
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(collection.name);
        
        TextView entries = (TextView) item.findViewById(R.id.entries);
        entries.setText(String.format("%s entries", collection.getTotalLinksCount()));
        
        TextView stats = (TextView) item.findViewById(R.id.stats);
        stats.setText(String.format("by %s", collection.user.getFullName()));
        
        ImageView cover = (ImageView) item.findViewById(R.id.cover);
        
        ImageView profile_pic = (ImageView) item.findViewById(R.id.profile_pic);
        
        if (convertView == null) {
        	 title.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
        	 entries.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
        	 stats.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
        }
        
        if (collection.cover_asset != null) {
        	Picasso.with(mContext).load(collection.cover_asset.getSmallOrDefaultAsset()).into(cover);
        } else {
        	cover.setImageResource(android.R.color.transparent);
        }
        
        if (collection.user.image_url != null) {
        	Picasso.with(mContext).load(collection.user.image_url).into(profile_pic);
        	profile_pic.setVisibility(View.VISIBLE);
        } else {
        	profile_pic.setVisibility(View.GONE);
        }
        
        return item;
    }
}