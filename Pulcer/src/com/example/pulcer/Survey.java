/**
 * Copyright (c) 2011, 2012 Sentaca Communications Ltd.
 */
package com.example.pulcer;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.pulcer.widget.AccordionView;

public class Survey extends Activity
{
	boolean isBraden=false;
	
	private static final String TAG = "AccordionWidgetDemoActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			isBraden = extras.getBoolean("IS_BRADEN");
		if(isBraden)
			createBraden();
		else
			createUlcerSurvey();
	}
	
	public void createBraden()
	{
		setContentView(R.layout.braden);
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.example_get_by_id);
		TextView tv = new TextView(this);
		tv.setText("Added in runtime...");
//		FontUtils.setCustomFont(tv, getAssets());
		ll.addView(tv);
	}
	
	public void createUlcerSurvey()
	{
		setContentView(R.layout.ulcer_survey);
	}
}