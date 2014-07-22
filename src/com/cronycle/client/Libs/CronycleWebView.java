package com.cronycle.client.Libs;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class CronycleWebView extends WebView {
	
	private OnScrollChangedCallback mOnScrollChangedCallback;
	
	public CronycleWebView(Context context) {
		super(context);
	}
	
	public CronycleWebView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
	
    public CronycleWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
	}
	
	public OnScrollChangedCallback getOnScrollChangedCallback()
    {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
    {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }
	
    public static interface OnScrollChangedCallback
    {
        public void onScroll(int l, int t, int oldl, int oldt);
    }
}
