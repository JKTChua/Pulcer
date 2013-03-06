package com.example.pulcer;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.me.pulcer.common.PApp;
import com.me.pulcer.parser.LoginParser;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.AsyncCall;
import com.me.pulcer.web.AsyncCallListener;
import com.me.pulcer.web.RequestMethod;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	EditText mName, mEmail, mPassword, mPasswordConf;
	Button mMale, mFemale;
	DatePicker mDate;
	String dateString;
	public AsyncCall asyncCall;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
		mEmail = (EditText)findViewById(R.id.txtRegisterEmail);
		mPassword = (EditText)findViewById(R.id.txtRegisterPassword);
		mPasswordConf = (EditText)findViewById(R.id.txtRegisterPasswordConfirm);
		mMale = (Button)findViewById(R.id.btnMale);
		mFemale = (Button)findViewById(R.id.btnFemale);
		mDate = (DatePicker)findViewById(R.id.dateDob);
		
		mMale.setSelected(true);
		mMale.setOnClickListener(genderListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
	    
		return true;
	}
	
	//set preference for access token
    public void setPref(String key,String value)
    {
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
    
    //set preference for userID
    public void setPref(String key,int value)
    {
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
    
    protected void infoLog(String msg)
    {
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		PLogger.getLogger().info(str+"=>"+msg);
	}
	
	protected void infoError(String messge)
	{
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		str="***ERROR***\nERROR ON:"+str+":=>";
		PLogger.getLogger().info(str+messge);	
	}
	
	protected void infoError(String msg,Exception e)
	{
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		PLogger.getLogger().info("userMessage:=>"+msg);
		PLogger.getLogger().info("ERROR on"+str+"=>message:"+e.getMessage());
		PLogger.getLogger().info("cause:"+e.getCause());
		e.printStackTrace();
	}
	
	protected void showErrorMessage(String message,String title)
	{
		AlertDialog dialog=new AlertDialog.Builder(this).create();
		dialog.setTitle(title);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage(message);
		dialog.setButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	View.OnClickListener genderListener=new View.OnClickListener()
	{	
		@Override
		public void onClick(View v)
		{
			if(v.getId() == R.id.btnMale)
				mFemale.setSelected(false);
			else
				mMale.setSelected(false);
			v.setSelected(true);
		}
	};

	protected void register()
	{
		boolean isMale = mMale.isPressed();
		int month = mDate.getMonth();
		int day = mDate.getDayOfMonth();
		int year = mDate.getYear();
		dateString = year + "-" + month + "-" + day;
		
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"register");
		asyncCall.addParam("profile_name", mName.getText().toString());
		asyncCall.addParam("email", mEmail.getText().toString());
		asyncCall.addParam("password", mPassword.getText().toString());
		asyncCall.addParam("birth_date", dateString);
		if(isMale)
			asyncCall.addParam("gender", "M");
		else
			asyncCall.addParam("gender", "F");
		asyncCall.setAsyncCallListener(new AsyncCallListener()
		{
			@Override
			public void onResponseReceived(String str)
			{
				infoLog("SignUpResponse:"+str);
				try{
					Gson gson=new Gson();
					LoginParser response=gson.fromJson(str, LoginParser.class);
					if(Util.validateStr(response.data.accessToken) && response.data.userID!=0){
						setPref(PApp.Pref_AccessToken,response.data.accessToken);
						setPref(PApp.Pref_UserID,response.data.userID);
						startActivity(new Intent(Register.this, UserAgreement.class));
						finish();
					}else{
						showErrorMessage(response.errorMsg, getString(R.string.dialog_title));
					}
				}catch(Exception e){
					infoError("", e);
				}
			}
			@Override
			public void onErrorReceived(String str) {
				showErrorMessage(str, getString(R.string.dialog_title));
				
			}
		});
		asyncCall.execute();
	}
	
	private boolean validate()
	{
		if(mName.getText().toString().trim().length()==0)
		{
			Toast.makeText(this, getString(R.string.validate_name_msg), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mEmail.getText().toString().trim().length()==0)
		{
			Toast.makeText(this, getString(R.string.validate_email_msg), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(Util.validateEmail(mEmail.getText().toString())!=true)
		{
			Toast.makeText(this, getString(R.string.validate_email_invalid_msg), Toast.LENGTH_SHORT).show();
			return false;
		}
		if(mPassword.getText().toString().trim().length()==0)
		{
			Toast.makeText(this, getString(R.string.validate_pwd_msg), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(dateString.length()<=0)
		{
			Toast.makeText(this, getString(R.string.validate_bday_msg), Toast.LENGTH_SHORT).show();
			return false;
		}
		SimpleDateFormat dispFormate=new SimpleDateFormat("yyyy-MM-dd");
		if(Util.validateStr(dateString))
		{
			Date date;
			try
			{
				date = dispFormate.parse(dateString);
				Date curDate=new Date();
				if(date.getTime()>curDate.getTime())
				{
					Toast.makeText(this, getString(R.string.validate_bdate_msg), Toast.LENGTH_SHORT).show();
					return false;
				}
			} catch (java.text.ParseException e)
			{
			}
		}
		return true;
	}
}
