package com.getplusapp.mobile.android.component;


import com.getplusapp.mobile.android.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;



public class MyTabBar extends LinearLayout{
	
	Context context;
	int lastSelected=0;
	MyTabChangeListner listener;
	int textViewId=0;
	
	public MyTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		setOrientation(LinearLayout.HORIZONTAL);
		this.setPadding(0, 0, 0, 0);
	}
	
	public void addTab(View view,int tag){
		view.setTag(tag);
		if(lastSelected==0){
			lastSelected=(Integer) view.getTag();
			setSelected(view);
		}
		
		view.setOnClickListener(tabClick);
		LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.weight=1;
		view.setLayoutParams(params);
		addView(view);
	}
	
	public void setTextViewId(int id){
		this.textViewId=id;
	}
	
	public void setTabChangeListner(MyTabChangeListner listener){
		this.listener=listener;
	}
	
	View.OnClickListener tabClick=new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(lastSelected!=(Integer)v.getTag()&& listener!=null){
				listener.onTabChanged((Integer) v.getTag());
			}
			setSelected(v);
		}
	};
	 void setSelected(View view){
		setDefalt();
		view.setSelected(true);
		/*if(textViewId!=0){
			
			PLogger.getLogger().info("setting gray:"+view.getTag()+" id:"+textViewId);
		}*/
		//((PTextView)view.findViewById(R.id.tab_text)).setTextColor(R.color.txt_gray);
		lastSelected=(Integer) view.getTag();
		
	}
	public void setSelectedTab(int index){
		setSelected(getChildAt(index));
	}
	void setDefalt(){
		for(int i=0;i<this.getChildCount();i++){
			this.getChildAt(i).setSelected(false);
			/*((PTextView)this.getChildAt(i).findViewById(R.id.tab_text)).setTextColor(R.color.Green);
			((PTextView)this.getChildAt(i).findViewById(R.id.tab_text)).invalidate();*/
		}
		
	}
	public interface MyTabChangeListner{
		void onTabChanged(int tag);
	}
	
	public void setBadge(String message,int tab){
		((PTextView)this.getChildAt(tab).findViewById(R.id.badge_txvw)).setText(message);
		
	}
	

}
