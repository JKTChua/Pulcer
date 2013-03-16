package com.example.pulcer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	
	boolean isEdit=false;
	
	EditText mName, mEmail, mPassword, mPasswordConf;
	Button mMale, mFemale;
	DatePicker mDate;
	String dateString;
	boolean isMale;
	public AsyncCall asyncCall;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mName = (EditText)findViewById(R.id.txtName);
		mEmail = (EditText)findViewById(R.id.txtRegisterEmail);
		mPassword = (EditText)findViewById(R.id.txtRegisterPassword);
		mPasswordConf = (EditText)findViewById(R.id.txtRegisterPasswordConfirm);
		mMale = (Button)findViewById(R.id.btnMale);
		mFemale = (Button)findViewById(R.id.btnFemale);
		mDate = (DatePicker)findViewById(R.id.dateDob);
		
		mMale.setSelected(true);
		mMale.setOnClickListener(genderListener);
		mFemale.setOnClickListener(genderListener);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
			isEdit = extras.getBoolean("IS_EDIT");
		if(isEdit)
			loadEditData();
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
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERENCE, MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
    
    //set preference for userID
    public void setPref(String key,int value)
    {
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERENCE, MODE_PRIVATE);
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
	
	public void next(View v)
	{
		if(isEdit)
			editProfile();
		else
			register();
	}

	protected void register()
	{
		isMale = mMale.isPressed();
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
		asyncCall.addParam("device_token", "00");
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
						setPref(PApp.Pref_User_Profile_name, response.data.profileName);
						setPref(PApp.Pref_User_Email, response.data.email);
						setPref(PApp.Pref_User_BDate, response.data.birthData);
						setPref(PApp.Pref_User_gender, response.data.gender);
						startActivity(new Intent(Register.this, Dashboard.class));
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
	
	protected void editProfile()
	{
		int month = mDate.getMonth();
		int day = mDate.getDayOfMonth();
		int year = mDate.getYear();
		dateString = year + "-" + month + "-" + day;
		
		asyncCall=new AsyncCall(this);
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"updateloggedinuser");
		asyncCall.setMessage(getString(R.string.progress_title_updating_profile));
		asyncCall.isTouploadFile=true;
//		if(Util.validateStr(imageFile)){
//			asyncCall.addFile("file_0", new File(imageFile));	
//		}
		asyncCall.addParam("user_id", ""+getIntPref(PApp.Pref_UserID));
		asyncCall.addParam("profile_name", mName.getText().toString());
		/*asyncCall.addParam("email", email_edxt.getText().toString());*/
		if(Util.validateStr(mPassword.getText().toString())){
			asyncCall.addParam("old_password", mPassword.getText().toString());
			asyncCall.addParam("password", mPasswordConf.getText().toString());
		}
		asyncCall.addParam("birth_date", dateString);
		asyncCall.addParam("access_token", getStrPref(PApp.Pref_AccessToken));
		
		if(isMale){
			asyncCall.addParam("gender", "M");
		}else{
			asyncCall.addParam("gender", "F");
		}
		asyncCall.setAsyncCallListener(new AsyncCallListener()
		{
			PApp app = (PApp) getApplication();
			@Override
			public void onResponseReceived(String str) {
				infoLog("UpdateUser Response:"+str);
				try{
					Gson gson=new Gson();
					LoginParser response=gson.fromJson(str, LoginParser.class);
					if(Util.validateStr(response.data.accessToken) && response.data.userID!=0)
					{
						setPref(PApp.Pref_User_Profile_name, response.data.profileName);
						setPref(PApp.Pref_User_Email, response.data.email);
						setPref(PApp.Pref_User_BDate, response.data.birthData);
						setPref(PApp.Pref_User_gender, response.data.gender);
						if(Util.validateStr(response.data.photoUrl)){
							setPref(PApp.Pref_User_photoUrl,response.data.photoUrl);
						}
//						showSuccessDialog(R.drawable.item_updated, R.drawable.msg_profile_updated);
						Intent intent=new Intent(PApp.ACTION_GET_ALL_DETAIL);
						sendBroadcast(intent);
						app.isRefresUserData=true;
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
	
	protected void loadEditData()
	{
		PApp app = (PApp) this.getApplication();
		
		setTitle(getString(R.string.update_profile));
		((Button)findViewById(R.id.btnRegister)).setText(getString(R.string.btn_save));
		mPassword.setVisibility(View.VISIBLE);
		mPasswordConf.setVisibility(View.VISIBLE);
		mPassword.setHint(getString(R.string.password));
		if(Util.validateStr(getStrPref(PApp.Pref_User_Profile_name))){
			mName.setText(getStrPref(PApp.Pref_User_Profile_name));
		}
		if(Util.validateStr(getStrPref(PApp.Pref_User_Email))){
			mEmail.setText(getStrPref(PApp.Pref_User_Email));
		}
		if(Util.validateStr(getStrPref(PApp.Pref_User_gender))){
			String str=getStrPref(PApp.Pref_User_gender);
			if(str.equalsIgnoreCase("M")){
				isMale=true;
				mMale.setSelected(true);
				mFemale.setSelected(false);
				
			}else if(str.equalsIgnoreCase("F")){
				isMale=false;
				mMale.setSelected(false);
				mFemale.setSelected(true);
			}
			
		}
		
		if(Util.validateStr(getStrPref(PApp.Pref_User_BDate)))
		{
			dateString = getStrPref(PApp.Pref_User_BDate);
			System.out.println("DATE STRING: " + dateString);
			SimpleDateFormat dispFormat=new SimpleDateFormat("yyyy-MM-dd");
			Calendar date = Calendar.getInstance();
			try
			{
				date.setTime(dispFormat.parse(dateString));
				mDate.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
			} catch (java.text.ParseException e)
			{
			}
		}
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
		SimpleDateFormat dispFormat=new SimpleDateFormat("yyyy-MM-dd");
		if(Util.validateStr(dateString))
		{
			Date date;
			try
			{
				date = dispFormat.parse(dateString);
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
	
	public String getStrPref(String key)
	{
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERENCE, MODE_PRIVATE);
		return pref.getString(key, "");
	}
	
	public int getIntPref(String key)
	{
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERENCE, MODE_PRIVATE);
		return pref.getInt(key, 0);
	}
	
	
	public void login(View v)
	{
		startActivity(new Intent(Register.this, Login.class));
		finish();
	}
	
	public void agreement(View v)
	{
		startActivity(new Intent(Register.this, UserAgreement.class));
	}
}
