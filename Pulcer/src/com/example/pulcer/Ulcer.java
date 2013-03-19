package com.example.pulcer;

import java.io.File;
import java.util.ArrayList;

import com.me.pulcer.adapter.UlcerAdapter;
import com.me.pulcer.adapter.UlcerGalleryAdapter;
import com.me.pulcer.entity.Braden;
import com.me.pulcer.entity.UlcerEnt;
import com.me.pulcer.entity.UlcerGroup;
import com.the9tcat.hadi.DefaultDAO;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class Ulcer extends Activity
{
	ArrayList<UlcerEnt> ulcerListData;
	Button btnCamera;
	Gallery gallery;
	UlcerGalleryAdapter adapter;
	int selected = 0;
	Intent cameraIntent;
	String path;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ulcer_view);
		gallery = (Gallery)findViewById(R.id.ulcer_gallery);
		btnCamera = (Button)findViewById(R.id.button_camera);
		loadFromDb();
		File file = new File(this.getExternalFilesDir(null), "");
		path = file.getAbsolutePath();
		cameraIntent = new Intent(this, CameraCap.class);
		
		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
			{
				populateForm(position);
				selected = position;
			}
		});
		
		btnCamera.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				int[] value = {ulcerListData.get(selected).ulcerId, ulcerListData.get(selected).groupId};
				cameraIntent.putExtra("ids", value);
				cameraIntent.putExtra("path", path);
				startActivity(cameraIntent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ulcer, menu);
		return true;
	}
	
	private void populateForm(int position)
	{
		selected = position;
		TextView tv = (TextView)findViewById(R.id.tv_Stage);
		tv.setText("Stage " + ulcerListData.get(position).stage);
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
		try
		{
			File file = new File(this.getExternalFilesDir(null), "ulcer-1-1.jpg");
//			File file = new File(Environment.getExternalStorageDirectory() + "1-1.jpg");
			if (!file.exists())
			{
				file.createNewFile();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (ulcerListData == null)
		{
			ulcerListData = new ArrayList<UlcerEnt>();
			UlcerEnt ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "2/1/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 4;
			ulcerEnt.ulcerId = 1;
			ulcerEnt.internal = 3;
			ulcerEnt.image = "ulcer-2-1.jpg";
			ulcerListData.add(ulcerEnt);
			
			ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/16/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 3;
			ulcerEnt.ulcerId = 2;
			ulcerEnt.internal = 3;
			ulcerEnt.image = "ulcer-1-1.jpg";
			ulcerListData.add(ulcerEnt);
			
			ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/19/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 2;
			ulcerEnt.ulcerId = 3;
			ulcerEnt.internal = 3;
			ulcerEnt.image = "ulcer-1-1.jpg";
			ulcerListData.add(ulcerEnt);
			
			ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/20/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 1;
			ulcerEnt.ulcerId = 4;
			ulcerEnt.internal = 3;
			ulcerEnt.image = "ulcer-1-1.jpg";
			ulcerListData.add(ulcerEnt);
			
			ulcerEnt = new UlcerEnt();
			ulcerEnt.date = "3/22/2013";
			ulcerEnt.groupId = 2;
			ulcerEnt.healingStatus = 2;
			ulcerEnt.stage = 1;
			ulcerEnt.ulcerId = 5;
			ulcerEnt.internal = 3;
			ulcerEnt.image = "ulcer-1-1.jpg";
			ulcerListData.add(ulcerEnt);
		}
		adapter = new UlcerGalleryAdapter(this, this, ulcerListData);	
		gallery.setSpacing(0);
		gallery.setAdapter(adapter);
		TextView tv = (TextView)findViewById(R.id.tv_Stage);
		tv.setText("Stage " + ulcerListData.get(ulcerListData.size() - 1).stage);
//		gallery.setAdapter(new UlcerGalleryAdapter(this));
	}
	
}
