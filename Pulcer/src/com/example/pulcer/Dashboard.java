package com.example.pulcer;

import java.util.ArrayList;
import java.util.List;

import com.the9tcat.hadi.DefaultDAO;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.database.Cursor;

import com.me.pulcer.adapter.UlcerAdapter;
import com.me.pulcer.entity.UlcerEnt;
import com.me.pulcer.entity.UlcerGroup;

public class Dashboard extends Activity
{
	ListView ulcer_list;
	
	UlcerAdapter adapter;
	ArrayList <UlcerGroup> listData=new ArrayList<UlcerGroup>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		loadFromDb();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_launcher, menu);
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
	
	protected void loadFromDb()
	{
		DefaultDAO dao = new DefaultDAO(this);
		String args[]={""+0};
		try{
			listData = (ArrayList<UlcerGroup>) dao.select(UlcerEnt.class, false, "id>", args, null, null, null, null);
			adapter = new UlcerAdapter(this, listData);
			
		}catch(Exception e){
			//infoLog("Error while retriving data from db:"+e);
		}
	}
}
