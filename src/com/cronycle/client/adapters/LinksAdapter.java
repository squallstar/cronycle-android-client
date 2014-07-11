package com.cronycle.client.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cronycle.client.CronycleApplication;
import com.cronycle.client.R;
import com.cronycle.client.Libs.API;
import com.cronycle.client.Libs.API.OnFetchListener;
import com.cronycle.client.Libs.CronycleCollection;
import com.cronycle.client.Libs.CronycleLink;
import com.cronycle.client.Libs.ScaleToFitWidthHeightTransform;
import com.squareup.picasso.Callback;
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
        
    	if (!isFetching && (position*10/collection.links.size()) > 7 ) {
    		
    		isFetching = true;
    		final LinksAdapter adapter = this;
    		
    		API.Current().fetchMoreLinksForCollection(collection, new OnFetchListener() {
				
				@Override
				public void onComplete(Boolean success, int newLinksCount) {
					
					if (success && newLinksCount > 0) {
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
        
        final ImageView cover = (ImageView) item.findViewById(R.id.cover);        
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(link.getGoodLookingTitle());
        
        TextView sourceLink = (TextView) item.findViewById(R.id.sourceLink);
        sourceLink.setText(link.url.replace("http://", ""));
        
        TextView postedAgo = (TextView) item.findViewById(R.id.postedDate);
        postedAgo.setText(link.getPostedAgo());
        
        TextView description = (TextView) item.findViewById(R.id.description);
        description.setText(link.description);
        
        if (convertView == null) {
       	 title.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
       	 description.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
       	 sourceLink.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaSemiBold);
       	 postedAgo.setTypeface(((CronycleApplication)mContext.getApplicationContext()).proximaNovaRegular);
       	 
       	 sourceLink.setTextColor(collection.settings.getColor());
       }
        
       Boolean showImages = collection.settings.links.shouldDisplayImages();
        
       if (link.lead_image != null && showImages) {
        	
        	WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        	Display display = wm.getDefaultDisplay();
        	Point size = new Point();
        	display.getSize(size);
        	
        	int width = size.x - 80;
        	int height = width * link.lead_image.height / link.lead_image.width;
        	
        	cover.setLayoutParams(new LayoutParams(width, height));
        	
        	Picasso
        		.with(mContext)
        		.load(link.lead_image.getSmallOrDefaultAsset())
        		.transform(new ScaleToFitWidthHeightTransform(width, false))
        		.into(cover, new Callback() {
					
					@Override
					public void onSuccess() {
						if (cover.getVisibility() == View.GONE) {
							cover.setVisibility(View.VISIBLE);
						}
						
						//cover.setBackgroundColor(Color.WHITE);
					}

					@Override
					public void onError() {
						cover.setVisibility(View.GONE);
					}
				});
        }
       
        if (!showImages) {
        	cover.setVisibility(View.GONE);
        } else if (!collection.settings.links.shouldDisplayTitle()) {
        	title.setVisibility(View.GONE);
        	description.setVisibility(View.GONE);
        }

        return item;
    }
}
