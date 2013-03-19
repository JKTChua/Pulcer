package com.example.pulcer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.me.pulcer.entity.Braden;
import com.me.pulcer.widget.AccordionView;
import com.the9tcat.hadi.DefaultDAO;

public class BradenSurvey extends Activity
{
//	boolean isBraden=false;
	Braden old;
	
	RadioGroup sensory, moisture, activity, mobility, nutrition, friction, oxygenation;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		old = (Braden) getIntent().getSerializableExtra("Braden");
//		Bundle extras = getIntent().getExtras();
//		if(extras != null)
//			isBraden = extras.getBoolean("IS_BRADEN");
//		if(isBraden)
			createBraden();
//		else
//			createUlcerSurvey();
	}
	
	public void createBraden()
	{
		setContentView(R.layout.braden);
		sensory = (RadioGroup) findViewById(R.id.radioSensory);
		activity = (RadioGroup) findViewById(R.id.radioActivity);
		moisture = (RadioGroup) findViewById(R.id.radioMoisture);
		mobility = (RadioGroup) findViewById(R.id.radioMobility);
		nutrition = (RadioGroup) findViewById(R.id.radioNutrition);
		friction = (RadioGroup) findViewById(R.id.radioFriction);
		oxygenation = (RadioGroup) findViewById(R.id.radioOxygenation);
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		if(old != null)
		{
			if(old.sensoryPerception != 0)
			{
				RadioButton b = ((RadioButton)sensory.getChildAt(old.sensoryPerception-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(0)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.moisture != 0)
			{
				RadioButton b = ((RadioButton)moisture.getChildAt(old.moisture-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(1)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.activity != 0)
			{
				RadioButton b = ((RadioButton)activity.getChildAt(old.activity-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(2)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.mobility != 0)
			{
				RadioButton b = ((RadioButton)mobility.getChildAt(old.mobility-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(3)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.nutrition != 0)
			{
				RadioButton b = ((RadioButton)nutrition.getChildAt(old.nutrition-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(4)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.friction != 0)
			{
				RadioButton b = ((RadioButton)friction.getChildAt(old.friction-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(5)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
			if(old.oxygenation != 0)
			{
				RadioButton b = ((RadioButton)oxygenation.getChildAt(old.oxygenation-1));
				b.setChecked(true);
				((TextView)((LinearLayout)v.getChildAt(6)).getChildAt(0).findViewById(v.getSubtextId())).setText(b.getText());
			}
		}
	}
	
	public void updateSensorySubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(0)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateMoistureSubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(1)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateActivitySubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(2)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateMobilitySubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(3)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateNutritionSubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(4)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateFrictionSubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(5)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	public void updateOxygenationSubtext(View x)
	{
		final AccordionView v = (AccordionView) findViewById(R.id.accordion_view);
		((TextView)((LinearLayout)v.getChildAt(6)).getChildAt(0).findViewById(v.getSubtextId())).setText(((TextView)x).getText());
	}
	
	public void submitBraden(View v)
	{
		Braden survey = new Braden();
		
		survey.sensoryPerception = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)sensory.getChildAt(x)).isChecked())
				survey.sensoryPerception = (byte) (x+1);
		}
		
		survey.moisture = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)moisture.getChildAt(x)).isChecked())
				survey.moisture = (byte) (x+1);
		}
		
		survey.activity = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)activity.getChildAt(x)).isChecked())
				survey.activity = (byte) (x+1);
		}
		
		survey.mobility = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)mobility.getChildAt(x)).isChecked())
				survey.mobility = (byte) (x+1);
		}
		
		survey.nutrition = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)nutrition.getChildAt(x)).isChecked())
				survey.nutrition = (byte) (x+1);
		}
		
		survey.friction = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)friction.getChildAt(x)).isChecked())
				survey.friction = (byte) (x+1);
		}
		
		survey.oxygenation = 0;
		for(byte x = 0; x < 4; x++)
		{
			if(((RadioButton)oxygenation.getChildAt(x)).isChecked())
				survey.oxygenation = (byte) (x+1);
		}
//		int total = 0;
//		if(survey.moisture != 0)
//			total += 4;
//		if(survey.activity != 0)
//			total += 4;
//		if(survey.mobility != 0)
//			total += 4;
//		if(survey.nutrition != 0)
//			total += 4;
//		if(survey.friction != 0)
//			total += 4;
//		if(survey.sensoryPerception != 0)
//			total += 4;
//		if(survey.oxygenation != 0)
//			total += 4;
		survey.riskTotal = (byte) (survey.moisture + survey.activity + survey.mobility + survey.nutrition + survey.friction + survey.sensoryPerception + survey.oxygenation);
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		survey.date = df.format(c.getTime());
		
		DefaultDAO dao = new DefaultDAO(BradenSurvey.this);
		dao.insert(survey);
		finish();
	}
	
//	public void createUlcerSurvey()
//	{
//		setContentView(R.layout.ulcer_survey);
//	}
}