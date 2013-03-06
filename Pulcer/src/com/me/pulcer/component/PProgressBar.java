package com.getplusapp.mobile.android.component;


import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getplusapp.mobile.android.R;

public class PProgressBar extends RelativeLayout {

	Context context;
	public  final int initialMargin=12;
	private int progress=0;
	private int min=0;
	private int max=100;
	TextView grey,blue;
	
	
	public PProgressBar(Context context) {
		super(context);
		this.context=context;
		init();
	}
	
	public PProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		init();
	}
	
	public PProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context=context;
		init();	
	}
	
	private void init(){
		
		if(isInEditMode()!=true){
			this.setPadding(0, 0, 0, 0);
			RelativeLayout.LayoutParams bparams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			
			blue=new TextView(context);
			blue.setPadding(0, 0, 0, 0);
			blue.setText("");
			blue.setBackgroundResource(R.drawable.pg_blue_full);
			this.addView(blue,bparams);
			
			RelativeLayout.LayoutParams gparams=new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			grey=new TextView(context);
			
			if(this.getTag()!=null){
				grey.setBackgroundResource(R.drawable.black_pbar_2);
			}else{
				grey.setBackgroundResource(R.drawable.pg_blank_grey);
			}
			
			
			gparams.leftMargin=initialMargin;
			this.addView(grey,gparams);
			grey.setPadding(0, 0, 0, 0);
			
		}
		
		
	}
	
	public int getProgress(){
		return this.progress;
	}
	
	public void setProgress(double prg) throws IllegalArgumentException{
		double margin=0;
		if(prg>=this.min &&  prg<=max){
			if(min==0 && max==100){
				if(prg!=0){
					margin=(getCompWidth()*(prg/100));
					
//					PLogger.getLogger().info("prg:"+prg+" this.getMesuredWidth:"+this.getMeasuredWidth()+" compWidth:"+getCompWidth()+" computeMargin:"+margin);
				}
				
			}
			if(grey!=null){
				RelativeLayout.LayoutParams params=(LayoutParams) grey.getLayoutParams();
				params.leftMargin=(int) (margin+this.initialMargin);
				grey.setLayoutParams(params);
				grey.setPadding(0, 0, 0, 0);
				this.progress=(int) prg;
				
			}
			
		}else{
			throw new IllegalArgumentException();
		}
	}
	
	public double getCompWidth(){
		
		if(this.getMeasuredWidth()==0){
			WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(displaymetrics);
			return (double) (displaymetrics.widthPixels-10)-initialMargin;
		}else{
			return (double) this.getMeasuredWidth()-initialMargin;
		}
		
	}
	
	
	
	
	

}
