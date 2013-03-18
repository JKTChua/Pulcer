package com.example.pulcer;

import java.util.ArrayList;

import com.the9tcat.hadi.DefaultDAO;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

import com.me.pulcer.adapter.UlcerAdapter;
import com.me.pulcer.entity.Braden;
import com.me.pulcer.entity.UlcerEnt;
import com.me.pulcer.entity.UlcerGroup;

public class Dashboard extends Activity
{
	ListView ulcer_list;
	TextView risk;
	
	UlcerAdapter adapter;
	ArrayList <UlcerGroup> ulcerListData=new ArrayList<UlcerGroup>();
	ArrayList <Braden> bradenListData=new ArrayList<Braden>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		risk = (TextView) findViewById(R.id.tvRiskResult);
		loadFromDb();
		updateRisk();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
    
	AdapterView.OnItemClickListener listClick=new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3)
		{
			if(adapter.getItem(position)!=null)
			{
				UlcerGroup ulcer = adapter.getItem(position);
				Intent intent = new Intent(Dashboard.this, Ulcer.class);
				
//				intent.putExtra("MODE", AddReminder.MODE_ADD);
//				
//				intent.putExtra("Pill", adapter.getItem(postion));
//				intent.putExtra("SELECTED_USER_ID", selectedUserId);
//				startActivityForResult(intent, REQ_ADD_PILL);
			}
		}
	};
	
	public void openSettings(View v)
	{
		startActivity(new Intent(Dashboard.this, Settings.class));
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
	}
	
	public void newUlcer(View v)
	{
		startActivity(new Intent(Dashboard.this, Survey.class));
	}
	
	public void braden(View v)
	{
		startActivity(new Intent(Dashboard.this, Survey.class).putExtra("IS_BRADEN", true));
	}
	
	protected void loadFromDb()
	{
		DefaultDAO dao = new DefaultDAO(this);
		String args[]={""+0};
		try{
			ulcerListData = (ArrayList<UlcerGroup>) dao.select(UlcerEnt.class, false, "id>", args, null, null, null, null);
			bradenListData = (ArrayList<Braden>) dao.select(Braden.class, false, "id>", args, null, null, null, null);
			adapter = new UlcerAdapter(this, ulcerListData);
			
		}catch(Exception e){
			//infoLog("Error while retriving data from db:"+e);
		}
	}
	
	protected void updateRisk()
	{
		if(bradenListData.size() > 0)
			risk.setText(risk(bradenListData.get(bradenListData.size()-1).riskTotal));
		else
			risk.setText("Take Risk Assessment");
	}
	
	private String risk(int x)
	{
		if(x <= 9)
			return "Severe Risk";
		else if(x >= 10 && x <= 12)
			return "High Risk";
		else if(x == 13 || x == 14)
			return "Moderate Risk";
		else if(x >= 15 && x <= 18)
			return "At Risk";
		else
			return "No Risk";
	}
}
