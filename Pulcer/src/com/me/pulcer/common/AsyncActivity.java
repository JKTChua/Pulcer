package com.me.pulcer.common;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.component.PButton;
import android.component.PTextView;
import android.component.SlideMenu;
import android.entity.Reminder;
import android.entity.Test;
import android.parser.ReminderStatusParser;
import android.parser.Response;
import android.util.PLogger;
import android.util.Util;
import android.web.AsyncCall;
import android.web.AsyncCallListener;
import android.web.RequestMethod;
import com.google.gson.Gson;
import com.me.pulcer.R;
import com.the9tcat.hadi.DefaultDAO;

public abstract class AsyncActivity extends Activity
{	
	static final int UPCOMMING_FILTER=101;
	static final int TAKEN_FILTER=102;
	static final int MISSED_FILTER=103;
	
	public static final String STATUS_TAKE="take";
	public static final String STATUS_MISSED="missed";
	public static final String STATUS_SKIP="skip";
	
	
	public AsyncCall asyncCall;
	public boolean onBackFininish=true;
	protected SlideMenu menuController;
	protected PTextView title;
	protected PButton menuBtn,rightBtn,backBtn,cancelBtn,headerSave;
	private Dialog pillDialog;
	private InputMethodManager imm ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		asyncCall = (AsyncCall) getLastNonConfigurationInstance();
        if (asyncCall == null) 
        	asyncCall = new AsyncCall(this);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
	    boolean ret = super.dispatchTouchEvent(event);
	    View w = getCurrentFocus();
	    int scrcoords[] = new int[2];
	    if(w!=null){
	    	w.getLocationOnScreen(scrcoords);
	    	float x = event.getRawX() + w.getLeft() - scrcoords[0];
		    float y = event.getRawY() + w.getTop() - scrcoords[1];
		    if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
		    	imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
		    }
	    }
	    return ret;
	}
	
	@Override
	protected void onResume() {
		
		infoLog("Async onResume:"+asyncCall+" isRunning:"+asyncCall.isRunning+" pDialog:"+asyncCall.pDialog);

		if(asyncCall != null && asyncCall.isRunning && asyncCall.pDialog != null && !asyncCall.pDialog.isShowing()) {
			asyncCall.pDialog.setMessage(asyncCall.pbarMessage);
			asyncCall.pDialog.show();
		}
		else if(asyncCall != null && !asyncCall.isRunning && asyncCall.pDialog.isShowing()) {
			asyncCall.pDialog.dismiss();
		}
		
		super.onResume();
	}
	
	@Override
    public Object onRetainNonConfigurationInstance() {
    	return asyncCall;
    }

    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
	public void onBackPressed() {
		super.onBackPressed();
		if(onBackFininish)
			finish();
	}
    
    public boolean validateResponse(Response response){
    	boolean flag=false;
    	if( response!=null && response.statusCode==1000 && Util.validateStr(response.errorMsg)!=true ){
    		flag=true;
    	}
    	
    	return flag;
    }
    
    protected void initTitle(){
		title=(PTextView) findViewById(R.id.title_txvw);
		menuBtn=(PButton) findViewById(R.id.menu_btn);
		rightBtn=(PButton)findViewById(R.id.right_btn);
		backBtn=(PButton) findViewById(R.id.back_btn);
		cancelBtn=(PButton) findViewById(R.id.header_btn);
		headerSave=(PButton) findViewById(R.id.header_save_btn);
		
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
	
	protected void showErrorMessage(String message,String title)
	{
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
	
	protected void  showMessageDialog(String message,DialogInterface.OnClickListener  listener) {
		AlertDialog dialog=new AlertDialog.Builder(this).create();
		dialog.setTitle(getString(R.string.app_name));
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage(message);
		dialog.setButton("Ok", listener);
		dialog.show();
	}
	public void showMessageDialog(String message){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle(getString(R.string.app_name));
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage(message);
		dialog.setPositiveButton(getString(R.string.dlg_ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
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
    
	protected void showPillDialog(final Reminder medicine,final int userId,final AsyncCallListener listener) {
		pillDialog=new Dialog(this,R.style.PlusDialog);
		pillDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		pillDialog.setContentView(R.layout.pill_dialog);
		pillDialog.findViewById(R.id.i_tookit_dlg_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeMedicenState(STATUS_TAKE,userId,medicine.reminderId,listener);
				pillDialog.dismiss();
			}
		});
		pillDialog.findViewById(R.id.i_skippit_dlg_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeMedicenState(STATUS_SKIP,userId,medicine.reminderId,listener);
				pillDialog.dismiss();
			}
		});
		pillDialog.findViewById(R.id.i_missedit_dlg_btn).setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeMedicenState(STATUS_MISSED,userId,medicine.reminderId,listener);
				pillDialog.dismiss();
			}
		});
		
		((PTextView)pillDialog.findViewById(R.id.pill_name_dlg_txvw)).setText(medicine.medication);
		((PTextView)pillDialog.findViewById(R.id.pill_content_dlg_txvw)).setText(medicine.dosage);
		((PTextView)pillDialog.findViewById(R.id.pill_time_dlg_txvw)).setText(Util.getTweelHour(medicine.time));
		((PTextView)pillDialog.findViewById(R.id.pill_dosage_times)).setText(""+medicine.dosageTimes+"x");
		LinearLayout dayContainer=(LinearLayout) pillDialog.findViewById(R.id.day_container);
		Util.showDays(dayContainer, medicine.days);
		
		WindowManager.LayoutParams WMLP=pillDialog.getWindow().getAttributes();
		WMLP.width=WindowManager.LayoutParams.FILL_PARENT;
		pillDialog.getWindow().setAttributes(WMLP);
		pillDialog.show();
		
	}
	private void changeMedicenState(final String status,int UserId,final int reminderId, AsyncCallListener listener){
		infoLog("Status:"+status+" reminderID:"+reminderId);
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"statusupdate");
		asyncCall.setMessage(getString(R.string.progress_title));
		asyncCall.addParam("access_token",getStrPref(PApp.Pref_AccessToken));
		asyncCall.addParam("status",status);
		asyncCall.addParam("reminder_id",""+reminderId);
		asyncCall.addParam("user_id",""+UserId);
		asyncCall.addParam("lat",""+"102.12345");
		asyncCall.addParam("lng","75.25521");
		asyncCall.setAsyncCallListener(new AsyncCallListener() {
			
			@Override
			public void onResponseReceived(String str) {
				infoLog("userResponse:"+str);
				try{
					Gson gson=new Gson();
					ReminderStatusParser statusParser= gson.fromJson(str, ReminderStatusParser.class);
					if(validateResponse(statusParser)){
						if(Util.validateStr(statusParser.data.operation)){
							Toast.makeText(AsyncActivity.this, statusParser.data.operation, Toast.LENGTH_SHORT).show();
						}
					}else{
						showErrorMessage(statusParser.errorMsg, getString(R.string.dialog_title));
					}
				}catch(Exception e){
					e.printStackTrace();
					infoError("Error while updating status:", e);
				}
				
			}
			@Override
			public void onErrorReceived(String str) {
				showErrorMessage(str, getString(R.string.dialog_title));
				
			}
		});
		
		asyncCall.execute();
	}
    
	protected void chkDB(){
		try{
			DefaultDAO dao=new DefaultDAO(this);
			@SuppressWarnings("unchecked")
			List<Test> list=(List<Test>) dao.select(Test.class, false, null, null, null, null, null, null);
			if(list==null){
				
			}
			
		}catch(Exception e){
			infoLog("Error while chk db");
			e.printStackTrace();
		}
	}
	
	public void showSuccessDialog(int status,int message){
		final Animation fadeIn=AnimationUtils.loadAnimation(this, R.anim.fade_in);
		final Animation fadeIn2=AnimationUtils.loadAnimation(this, R.anim.fade_in);
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, null);
		Toast customToast=new Toast(this);
		customToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		customToast.setDuration(10000);
		customToast.setView(layout);
		customToast.setGravity(Gravity.FILL, 0, 0);
		customToast.setMargin(0, 0);
		customToast.show();
		final ImageView imgv=(ImageView) layout.findViewById(R.id.symbol_imgvw);
		final ImageView textimgv=(ImageView) layout.findViewById(R.id.text_imgvw);
		imgv.setImageResource(status);
		textimgv.setImageResource(message);
		imgv.startAnimation(fadeIn);
		fadeIn.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {}
			@Override
			public void onAnimationRepeat(Animation arg0) {}
			@Override
			public void onAnimationEnd(Animation arg0) {
				imgv.clearAnimation();
				imgv.setVisibility(View.VISIBLE);
				
			}
		});
		textimgv.postDelayed(new Runnable() {
			@Override
			public void run() {
				textimgv.startAnimation(fadeIn2);
			}
		}, 500);
		fadeIn2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				textimgv.clearAnimation();
				textimgv.setVisibility(View.VISIBLE);
			}
		});
	}
	
	public void showConfirmationDialog(String message,DialogInterface.OnClickListener listener)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(message);
		builder.setPositiveButton(getString(R.string.dlg_yes), listener);
		builder.setNegativeButton(getString(R.string.dlg_no), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	/*public boolean chkInternet(){
		ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.CONNECTED ||
		   cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED)
		{
			return true;
		}else{
			return false;
		}
		    
	}*/

}
