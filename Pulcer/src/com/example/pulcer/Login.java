package com.example.pulcer;

import com.google.gson.Gson;
import com.me.pulcer.common.PApp;
import com.me.pulcer.parser.Response;
import com.me.pulcer.parser.LoginParser;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.AsyncCall;
import com.me.pulcer.web.AsyncCallListener;
import com.me.pulcer.web.RequestMethod;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity
{

	EditText mEmail, mPassword;
	public AsyncCall asyncCall;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
		mEmail = (EditText)findViewById(R.id.txtEmail);
		mPassword   = (EditText)findViewById(R.id.txtPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
    public boolean validateResponse(Response response)
    {
    	boolean flag=false;
    	if( response!=null && response.statusCode==1000 && Util.validateStr(response.errorMsg)!=true ){
    		flag=true;
    	}
    	
    	return flag;
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
	
	public void login(View v)
	{	
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"login");
		asyncCall.setMessage("Authenticating please wait...");
		asyncCall.addParam("email", mEmail.getText().toString());
		asyncCall.addParam("password", mPassword.getText().toString());
		asyncCall.addParam("device_token", "00");
		asyncCall.setAsyncCallListener(new AsyncCallListener()
		{
			@Override
			public void onResponseReceived(String str)
			{
//				infoLog("Login Response:"+ str);
				
				Gson gson = new Gson();
				LoginParser response = gson.fromJson(str, LoginParser.class);
				
				if(validateResponse(response))
				{
					if(Util.validateStr(response.data.accessToken) && response.data.userID!=0)
					{
						setPref(PApp.Pref_AccessToken,response.data.accessToken);
						setPref(PApp.Pref_UserID,response.data.userID);
						setUserData(response);
						startActivity(new Intent(Login.this, Dashboard.class));
						finish();
					}
					else
					{
						infoError("INVALID ACCESS TOKEN");
					}
				}else{
					infoLog("Login failed:"+response.errorMsg);
					showErrorMessage(response.errorMsg, "Error");
				}
				
			}
			
			@Override
			public void onErrorReceived(String str)
			{
				infoLog("Error:"+str);	
				showErrorMessage(str, "Error");
			}
		});
		asyncCall.execute();
	}
	
	public void register(View v)
	{
		startActivity(new Intent(Login.this, Register.class));
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
		finish();
	}
	
	private void setUserData(LoginParser response)
	{
		if(response!=null && response.data!=null)
		{
			setPref(PApp.Pref_User_Profile_name, response.data.profileName);
			setPref(PApp.Pref_User_Email, response.data.email);
			setPref(PApp.Pref_User_BDate, response.data.birthData);
			setPref(PApp.Pref_User_gender, response.data.gender);
			setPref(PApp.Pref_User_photoUrl, response.data.photoUrl);
			
			setPref(PApp.Pref_User_is_app_alert, response.data.isAppAlert);
			setPref(PApp.Pref_User_is_calendar_alert, response.data.isCalenderAlert);
			setPref(PApp.Pref_User_is_email_alert, response.data.isEmailAlert);
			setPref(PApp.Pref_User_is_phone_alert, response.data.isPhoneAlert);
			
			setPref(PApp.Pref_User_alert_email, response.data.alertEmail);
			setPref(PApp.Pref_User_alert_phone, response.data.alertPhone);
		}
	}

}
