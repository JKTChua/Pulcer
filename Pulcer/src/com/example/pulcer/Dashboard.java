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

public class Dashboard extends Activity
{
	ListView ulcer_list;
	
	UlcerAdapter adapter;
	ArrayList <UlcerEnt> listData=new ArrayList<UlcerEnt>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_launcher, menu);
		return true;
	}
    
	AdapterView.OnItemClickListener listClick=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3) {
			if(adapter.getItem(position)!=null){
				UlcerEnt ulcer = adapter.getItem(position);
				Intent intent = new Intent(Dashboard.this, Ulcer.class);
				
//				intent.putExtra("MODE", AddReminder.MODE_ADD);
//				
//				intent.putExtra("Pill", adapter.getItem(postion));
//				intent.putExtra("SELECTED_USER_ID", selectedUserId);
//				startActivityForResult(intent, REQ_ADD_PILL);
			}
		}
	};
}
