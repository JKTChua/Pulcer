package com.getplusapp.mobile.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class LockedScrollView extends HorizontalScrollView {

	
	boolean mScrollable=false;
	public LockedScrollView(Context context) {
		super(context);
		init();
	}
	public LockedScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public LockedScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	private void init(){
		this.setHorizontalScrollBarEnabled(false);
		this.setHorizontalFadingEdgeEnabled(false);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mScrollable;
		
	}
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mScrollable) return super.onTouchEvent(ev);
                return mScrollable; 
            default:
                return super.onTouchEvent(ev);
        }
    }
	

}
