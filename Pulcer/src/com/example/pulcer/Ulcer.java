package com.example.pulcer;

import java.util.ArrayList;

import com.me.pulcer.adapter.UlcerAdapter;
import com.me.pulcer.adapter.UlcerGalleryAdapter;
import com.me.pulcer.entity.Braden;
import com.me.pulcer.entity.UlcerEnt;
import com.me.pulcer.entity.UlcerGroup;
import com.the9tcat.hadi.DefaultDAO;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Gallery;
import android.widget.TextView;

public class Ulcer extends Activity
{
	ArrayList<UlcerEnt> ulcerListData;
	Gallery gallery;
	UlcerGalleryAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ulcer_view);
		gallery = (Gallery)findViewById(R.id.ulcer_gallery);
		loadFromDb();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ulcer, menu);
		return true;
	}
	
	protected void loadFromDb()
	{
		try
		{
			DefaultDAO dao = new DefaultDAO(this);
			String args[]={""+0};
			ulcerListData = (ArrayList<UlcerEnt>) dao.select(UlcerEnt.class, false, "id>", args, null, null, null, null);
		}
		catch(Exception e)
		{
			Log.d(CLIPBOARD_SERVICE, "Didn't load");
			//infoLog("Error while retriving data from db:"+e);
		}
		
		if (ulcerListData == null)
		{
			ulcerListData = new ArrayList<UlcerEnt>();
			UlcerEnt ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/19/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 2;
			ulcerEnt.ulcerId = 1;
			ulcerEnt.internal = 3;			
			
			ulcerListData.add(ulcerEnt);
			
//			ulcerListData = new ArrayList<UlcerEnt>();
			ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/20/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 2;
			ulcerEnt.ulcerId = 1;
			ulcerEnt.internal = 3;			
			
			ulcerListData.add(ulcerEnt);
		}
		adapter = new UlcerGalleryAdapter(this, ulcerListData);	
		gallery.setSpacing(0);
		gallery.setAdapter(adapter);
		TextView tv = (TextView)findViewById(R.id.tv_Stage);
		tv.setText("Stage " + ulcerListData.get(ulcerListData.size() - 1).stage);
//		gallery.setAdapter(new UlcerGalleryAdapter(this));
	}
	
}
