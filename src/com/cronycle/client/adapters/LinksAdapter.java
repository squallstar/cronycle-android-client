package com.cronycle.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.API.OnFetchListener;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleLink;
import com.cronycle.client.Libs.ScaleToFitWidthHeightTransform;
import com.squareup.picasso.Picasso;

public class LinksAdapter extends BaseAdapter {

	private Context mContext;
	private CronycleCollection collection;
	
	private Boolean isFetching = false;

    public LinksAdapter(Context c, CronycleCollection collection) {
        this.mContext = c;
        this.collection = collection;
    }

    public int getCount() {
        return collection.links.size();
    }

    public CronycleLink getItem(int position) {
        return collection.links.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	if (!isFetching && position == collection.links.size()-1) {
    		
    		isFetching = true;
    		final int currentLength = collection.links.size();
    		final LinksAdapter adapter = this;
    		
    		API.Current().loadLinksForCollection(collection, new OnFetchListener() {
				
				@Override
				public void onComplete(Boolean success) {
					
					if (success && currentLength != collection.links.size()) {
						// New links
						adapter.notifyDataSetChanged();
					}
					
					isFetching = false;
				}
			});
    	}
    	
    	View item;
        
        CronycleLink link = getItem(position);
        
        if (convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(mContext);
        	
        	item = inflater.inflate(R.layout.collection_link, parent, false);
        } else {
        	item = (View) convertView;
        }
        
        ImageView cover = (ImageView) item.findViewById(R.id.cover);        
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(link.name);
        
        TextView sourceLink = (TextView) item.findViewById(R.id.sourceLink);
        sourceLink.setText(link.url.replace("http://", ""));
        
        TextView postedAgo = (TextView) item.findViewById(R.id.postedDate);
        postedAgo.setText(link.getPostedAgo());
        
        TextView description = (TextView) item.findViewById(R.id.description);
        description.setText(link.description);
        
        if (convertView == null) {
       	 title.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
       	 description.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
       	 sourceLink.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
       	 postedAgo.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
       	 
       	 sourceLink.setTextColor(collection.settings.getColor());
       }
        
        if (link.lead_image != null) {
        	Picasso
        		.with(mContext)
        		.load(link.lead_image.getSmallOrDefaultAsset())
        		.transform(new ScaleToFitWidthHeightTransform(cover.getWidth(), false))
        		.into(cover);
        }

        return item;
    }
}
