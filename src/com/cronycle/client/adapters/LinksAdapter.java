package com.cronycle.client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cronycle.client.R;
import com.cronycle.client.Libs.CronycleLink;

public class LinksAdapter extends BaseAdapter {

	private Context mContext;
	
	private CronycleLink[] links;

    public LinksAdapter(Context c, CronycleLink[] links) {
        this.mContext = c;
        this.links = links;
    }

    public int getCount() {
        return links.length;
    }

    public CronycleLink getItem(int position) {
        return links[position];
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	View item;
        
        CronycleLink link = getItem(position);
        
        if (convertView == null) {
        	LayoutInflater inflater = LayoutInflater.from(mContext);
        	
        	item = inflater.inflate(R.layout.collection_link, parent, false);
            //item.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, 400));
        } else {
        	item = (View) convertView;
        }
        
        
        TextView title = (TextView) item.findViewById(R.id.title);
        title.setText(link.name);

        return item;
    }
	
}
