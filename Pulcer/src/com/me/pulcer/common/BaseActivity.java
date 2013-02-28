package com.me.pulcer.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.getplusapp.mobile.android.R;
import com.getplusapp.mobile.android.component.PButton;
import com.getplusapp.mobile.android.component.PTextView;
import com.getplusapp.mobile.android.component.SlideMenu;
import com.getplusapp.mobile.android.util.PLogger;



public class BaseActivity extends Activity {
	
	
	protected SlideMenu menuController;
	protected PTextView title;
	protected PButton menuBtn,rightBtn,leftBtn,backBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected void initTitle(){
		title=(PTextView) findViewById(R.id.title_txvw);
		menuBtn=(PButton) findViewById(R.id.menu_btn);
		rightBtn=(PButton)findViewById(R.id.right_btn);
		leftBtn=(PButton) findViewById(R.id.left_btn);
		backBtn=(PButton) findViewById(R.id.back_btn);
	}
	
	
	public static Bitmap createRoundedBitmap(Bitmap bitmap){
		Bitmap circleBitmap=null;
		if(bitmap!=null){
			circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			BitmapShader shader = new BitmapShader (bitmap,  TileMode.CLAMP, TileMode.CLAMP);
			Paint paint = new Paint();
			paint.setShader(shader);
			Canvas c = new Canvas(circleBitmap);
			c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
		}
		
		return circleBitmap;
	}
	
	protected void infoLog(String msg){
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		PLogger.getLogger().info(str+"=>"+msg);
		
	}
	
	protected void infoError(String messge) {
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		str="***ERROR***\nERROR ON:"+str+":=>";
		PLogger.getLogger().info(str+messge);
		
	}
	protected void infoError(String msg,Exception e){
		String str=this.getClass().getName();
		str=str.substring(str.lastIndexOf(".")+1, str.length());
		PLogger.getLogger().info("userMessage:=>"+msg);
		PLogger.getLogger().info("ERROR on"+str+"=>message:"+e.getMessage());
		PLogger.getLogger().info("cause:"+e.getCause());
		e.printStackTrace();
	}
	
	protected void showErrorMessage(String message,String title){
		
		AlertDialog dialog=new AlertDialog.Builder(this).create();
		dialog.setTitle(title);
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage(message);
		dialog.setButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
		
	}
	
	protected void  showMessage(String message,DialogInterface.OnClickListener  listener) {
		AlertDialog dialog=new AlertDialog.Builder(this).create();
		dialog.setTitle(getString(R.string.app_name));
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage(message);
		dialog.setButton("Ok", listener);
		dialog.show();
	}
	public String getStrPref(String key){
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		return pref.getString(key, "");
	}
	public int getIntPref(String key){
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		return pref.getInt(key, 0);
		
	}
	
	public void setPref(String key,String value){
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public void setPref(String key,int value){
		SharedPreferences pref = getSharedPreferences(PApp.PLUS_PREFERANCE, MODE_PRIVATE);
		SharedPreferences.Editor editor=pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public boolean chkInternet(){
		ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.CONNECTED ||
		   cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED)
		{
			return true;
		}else{
			return false;
		}
		    
	}
}
