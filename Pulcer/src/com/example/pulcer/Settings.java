package com.example.pulcer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Settings extends Activity
{
	
	private User list[];
	
	private class User
	{
		String name;
		public User(String user_name)
		{
			name = user_name;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	private boolean populateUserList()
	{
		return true;
	}

}
