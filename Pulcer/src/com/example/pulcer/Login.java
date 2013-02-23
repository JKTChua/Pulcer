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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		mEmail = (EditText)findViewById(R.id.txtEmail);
	    mPassword   = (EditText)findViewById(R.id.txtPassword);
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
	
	protected void login()
	{	
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"login");
		asyncCall.setMessage("Authenticating please wait...");
		asyncCall.addParam("email", mEmail.getText().toString());
		asyncCall.addParam("password", mPassword.getText().toString());
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

}
