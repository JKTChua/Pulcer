package com.example.pulcer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Settings extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public void editProfile(View v)
	{
		Intent intent=new Intent(Settings.this, Register.class);
		intent.putExtra("IS_EDIT", true);
		startActivity(intent);
	}
	
	public void logout(View v)
	{
		Intent intent=new Intent(Settings.this, Login.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	public void terms(View v)
	{
		startActivity(new Intent(Settings.this, UserAgreement.class));
	}
	
	public void back(View v)
	{
		finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
	}

}
