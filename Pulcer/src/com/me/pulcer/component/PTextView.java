package com.me.pulcer.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class PTextView extends TextView{

	
	private static final String FONT_PATH="fonts/HelveticaNeueLTPro-Bd.otf";
	private Typeface tf;
	
	public PTextView(Context context) {
		super(context);
		setFont(context);
	}
	public PTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont(context);
	}
	public PTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont(context);
	}
	private void setFont(Context context){
		if(!isInEditMode()){
			tf=Typeface.createFromAsset(context.getAssets(), FONT_PATH);
			this.setTypeface(tf);
		}
	}
	

}
