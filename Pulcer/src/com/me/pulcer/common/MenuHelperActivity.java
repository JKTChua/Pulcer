package com.me.pulcer.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.me.pulcer.DocView;
import com.me.pulcer.DocView_;
import com.me.pulcer.PillList_;
import com.me.pulcer.R;
import com.me.pulcer.SignUp_;
import com.me.pulcer.StartUp_;
import com.me.pulcer.adapter.FamilyAdapter;
import com.me.pulcer.component.PButton;
import com.me.pulcer.component.PEditText;
import com.me.pulcer.component.PTextView;
import com.me.pulcer.component.Pbar;
import com.me.pulcer.parser.LoginParser;
import com.me.pulcer.parser.UserDetailParser;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.AsyncCall;
import com.me.pulcer.web.AsyncCallListener;
import com.me.pulcer.web.RequestMethod;
import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class MenuHelperActivity extends AsyncActivity{
	
	
	public static final int DURATION=300;
	
	LinearLayout contentPan;
	PTextView menu_uname,plus_txvw,streak_txvw,position_txvw;
	//PProgressBar menu_progress;
	Pbar menu_progress;
	public ImageButton settingBtn,plusBtn,AddUserBtn;
	PButton settingBack,alertBack;
	ViewAnimator menuAnimator;
	public ImageView menu_avatar_imgvw;
	public ListView family_list;
	PApp app;
	public RelativeLayout current_user_container;
	PEditText alertPhone,alertEmail;
	PTextView editProfile,privacy,terms,logout,alerts;
	Button tgl_application,tgl_calender,tgl_email_btn,tgl_phone_btn,alert_save_btn;
	
	
	
	TranslateAnimation menuOpen,menuClose;
	Animation slideIn,slideOut,slideStatic;
	public FamilyAdapter familyAdapter;
	
	public Boolean isMenuOut=false;
	public UserDetailParser response;
	public Bitmap uImage=null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=(PApp) getApplication();
		slideIn=AnimationUtils.loadAnimation(this, R.anim.slide_in);
		slideOut=AnimationUtils.loadAnimation(this, R.anim.slide_out);
		slideStatic=AnimationUtils.loadAnimation(this, R.anim.static_anim);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && isMenuOut){
			toggleMenu();
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
		
		
	}
	
	public void initMenu(){
		initView();
		initAnimation();
	}
	
	protected void toggleMenu(){
		if(isMenuOut){
			menuBtn.setSelected(false);
			contentPan.startAnimation(menuClose);
		}else{
			menuBtn.setSelected(true);
			contentPan.startAnimation(menuOpen);
		}
	}
	
	protected void closeStatic(){
		AbsoluteLayout.LayoutParams lp=new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);
		contentPan.setLayoutParams(lp);
		isMenuOut=false;
		menuBtn.setSelected(false);
	}
	
	protected void initAnimation(){
		
		int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
		int displayWidth = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		
		final int shift=displayWidth-margin;
		menuOpen= new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);
		menuOpen.setDuration(DURATION);
		menuOpen.setFillAfter(true);
		menuOpen.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				contentPan.clearAnimation();
				AbsoluteLayout.LayoutParams lp=new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, shift, 0);
				contentPan.setLayoutParams(lp);
				isMenuOut=true;
				/*contentPan.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						toggleMenu();
						contentPan.setOnClickListener(null);
					}
				});*/
				
			}
		});
		
		menuClose= new TranslateAnimation(
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, -shift,
				TranslateAnimation.ABSOLUTE, 0,
				TranslateAnimation.ABSOLUTE, 0
				);
		menuClose.setDuration(DURATION);
		menuClose.setFillAfter(true);
		menuClose.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				contentPan.clearAnimation();
				AbsoluteLayout.LayoutParams lp=new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 0, 0);
				contentPan.setLayoutParams(lp);
				isMenuOut=false;
			}
		});
	}
	
	public void setUpMenuView(){
		if(response!=null && response.data!=null){
			if(response.data.userInfo!=null){
				if(response.data.userInfo.id==getIntPref(PApp.Pref_UserID)){
					if(Util.validateStr(response.data.userInfo.user_name)){
						menu_uname.setText(response.data.userInfo.user_name);
					}
					menu_progress.setProgress(response.data.userInfo.streakGraph);
					/*if(Util.validateStr(response.data.userInfo.photoUrl) && uImage!=null ){
						menu_avatar_imgvw.setImageBitmap(uImage);
					}*/
				}
				if(Util.validateStr(response.data.userInfo.plus)){
					plus_txvw.setText(response.data.userInfo.plus);
				}
				if(Util.validateStr(response.data.userInfo.streak)){
					streak_txvw.setText(response.data.userInfo.streak);
				}
				if(Util.validateStr(response.data.userInfo.position)){
					position_txvw.setText("#"+response.data.userInfo.position);
				}
			}
			
		}
		
	}
	
	
	public void setUpMenuForOther(){
		if(response!=null && response.data!=null){
			if(response.data.userInfo!=null){
				
				if(Util.validateStr(response.data.userInfo.user_name)){
					menu_uname.setText(response.data.userInfo.user_name);
				}
				menu_progress.setProgress(response.data.userInfo.streakGraph);
				if(Util.validateStr(response.data.userInfo.photoUrl) && uImage!=null ){
					menu_avatar_imgvw.setImageBitmap(uImage);
				}
				if(Util.validateStr(response.data.userInfo.plus)){
					plus_txvw.setText(response.data.userInfo.plus);
				}
				if(Util.validateStr(response.data.userInfo.streak)){
					streak_txvw.setText(response.data.userInfo.streak);
				}
				if(Util.validateStr(response.data.userInfo.position)){
					position_txvw.setText("#"+response.data.userInfo.position);
				}
				if(response.data.familyList!=null){
					familyAdapter=new FamilyAdapter(MenuHelperActivity.this, response.data.familyList,app.imageLoader);
					family_list.setAdapter(familyAdapter);
				}
			}
			
		}
	}
	
	protected void initView(){
		contentPan=(LinearLayout) findViewById(R.id.user_content_pan);
		menu_uname=(PTextView) findViewById(R.id.menu_uname);
		plus_txvw=(PTextView) findViewById(R.id.plus_txvw);
		streak_txvw=(PTextView) findViewById(R.id.streak_txvw);
		position_txvw=(PTextView) findViewById(R.id.position_txvw);
		menu_progress=(Pbar) findViewById(R.id.menu_progress);
		menu_avatar_imgvw=(ImageView) findViewById(R.id.menu_avatar_imgvw);
		family_list=(ListView) findViewById(R.id.family_list);
		plusBtn=(ImageButton) findViewById(R.id.menu_plus_btn);
		settingBtn=(ImageButton) findViewById(R.id.menu_setting_ibtn);
		AddUserBtn=(ImageButton) findViewById(R.id.setting_adduser_btn);
		menuAnimator=(ViewAnimator) findViewById(R.id.menu_animator);
		settingBack=(PButton) findViewById(R.id.setting_back);
		alertBack=(PButton) findViewById(R.id.back_alert_btn);
		editProfile=(PTextView) findViewById(R.id.opt_edit_profile);
		terms=(PTextView) findViewById(R.id.opt_terms);
		logout=(PTextView) findViewById(R.id.opt_logout);
		alerts=(PTextView) findViewById(R.id.opt_alerts);
		privacy=(PTextView)findViewById(R.id.opt_privacy);
		tgl_application=(Button) findViewById(R.id.tgl_application);
		tgl_calender=(Button) findViewById(R.id.tgl_calender);
		tgl_email_btn=(Button) findViewById(R.id.tgl_email_btn);
		tgl_phone_btn=(Button) findViewById(R.id.tgl_phone_btn);
		alertEmail=(PEditText) findViewById(R.id.alert_email_edtx);
		alertPhone=(PEditText) findViewById(R.id.alert_phone_edtx);
		alert_save_btn=(Button) findViewById(R.id.alert_save_btn);
		alert_save_btn.setOnClickListener(saveSettingClick);
		current_user_container=(RelativeLayout) findViewById(R.id.main_user_container);
		
		
		tgl_application.setOnClickListener(toogleClickListener);
		tgl_calender.setOnClickListener(toogleClickListener);
		tgl_email_btn.setOnClickListener(toogleClickListener);
		tgl_phone_btn.setOnClickListener(toogleClickListener);
		
		
		
		logout.setOnClickListener(settingOptionsClick);
		alerts.setOnClickListener(settingOptionsClick);
		terms.setOnClickListener(settingOptionsClick);
		privacy.setOnClickListener(settingOptionsClick);
		editProfile.setOnClickListener(settingOptionsClick);
		
		
		alertBack.setOnClickListener(listener);
		settingBack.setOnClickListener(listener);
		plusBtn.setOnClickListener(listener);
//		AddUserBtn.setOnClickListener(listener);
		settingBtn.setOnClickListener(listener);
		
	}
	
	View.OnClickListener settingOptionsClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.opt_edit_profile){
				Intent intent=new Intent(MenuHelperActivity.this, SignUp_.class);
				intent.putExtra("IS_EDIT", true);
				startActivity(intent);
			}else if(v.getId()==R.id.opt_logout){
				logout();
			}else if(v.getId()==R.id.opt_alerts){
				loadAlertView();
			}else if(v.getId()==R.id.opt_terms){
				Intent intent=new Intent(MenuHelperActivity.this, DocView_.class);
				intent.putExtra("MODE", DocView.MODE_TERMS);
				startActivity(intent);
				
			}else if(v.getId()==R.id.opt_privacy){
				Intent intent=new Intent(MenuHelperActivity.this, DocView_.class);
				intent.putExtra("MODE", DocView.MODE_PRIVACY);
				startActivity(intent);
			}
		}
	};
	
	void logout(){
		Intent intent=new Intent(MenuHelperActivity.this, StartUp_.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		app.stopSyncService();
		finish();
	}
	
	void loadAlertView(){
	
		String isAppAlert=getStrPref(PApp.Pref_User_is_app_alert);
		if(Util.validateStr(isAppAlert)  && isAppAlert.equalsIgnoreCase("Y") ){
			tgl_application.setSelected(true);
		}else if(Util.validateStr(isAppAlert)  && isAppAlert.equalsIgnoreCase("N")){
			tgl_application.setSelected(false);
		}
		
		String isCalAlert=getStrPref(PApp.Pref_User_is_calendar_alert);
		if(Util.validateStr(isCalAlert)  && isCalAlert.equalsIgnoreCase("Y") ){
			tgl_calender.setSelected(true);
		}else if(Util.validateStr(isCalAlert)  && isCalAlert.equalsIgnoreCase("N")){
			tgl_calender.setSelected(false);
		}
		
		String isEmailAlert=getStrPref(PApp.Pref_User_is_email_alert);
		if(Util.validateStr(isEmailAlert)  && isEmailAlert.equalsIgnoreCase("Y") ){
			tgl_email_btn.setSelected(true);
			if(Util.validateStr(getStrPref(PApp.Pref_User_alert_email))){
				alertEmail.setText(getStrPref(PApp.Pref_User_alert_email));
			}
		}else if(Util.validateStr(isEmailAlert)  && isEmailAlert.equalsIgnoreCase("N")){
			tgl_email_btn.setSelected(false);
		}
		
		String isPhoneAlert=getStrPref(PApp.Pref_User_is_phone_alert);
		if(Util.validateStr(isPhoneAlert)  && isPhoneAlert.equalsIgnoreCase("Y") ){
			tgl_phone_btn.setSelected(true);
			if(Util.validateStr(getStrPref(PApp.Pref_User_alert_phone))){
				alertPhone.setText(getStrPref(PApp.Pref_User_alert_phone));
			}
		}else if(Util.validateStr(isPhoneAlert)  && isPhoneAlert.equalsIgnoreCase("N")){
			tgl_phone_btn.setSelected(false);
		}
		menuAnimator.setInAnimation(slideOut);
		menuAnimator.setOutAnimation(slideStatic);
		menuAnimator.showNext();
		
	}
	
	View.OnClickListener saveSettingClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(validateAlert()){
				saveAlertSettings();
			}
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}
	};
	
	View.OnClickListener toogleClickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.isSelected()){
				v.setSelected(false);
			}else{
				v.setSelected(true);
			}
		}
	};

	View.OnClickListener listener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(v.getId()==R.id.menu_plus_btn){
				startActivity(new Intent(MenuHelperActivity.this,PillList_.class));
			}else if(v.getId()==R.id.setting_adduser_btn){
				
			}else if(v.getId()==R.id.menu_setting_ibtn){
				menuAnimator.setInAnimation(slideOut);
				menuAnimator.setOutAnimation(slideStatic);
				menuAnimator.showNext();
			}else if(v.getId()==R.id.setting_back){
				
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				menuAnimator.setInAnimation(slideStatic);
				menuAnimator.setOutAnimation(slideIn);
				menuAnimator.showPrevious();
			}else if(v.getId()==R.id.back_alert_btn){
				menuAnimator.setInAnimation(slideStatic);
				menuAnimator.setOutAnimation(slideIn);
				menuAnimator.showPrevious();
			}
			
		}
	};
	
	 boolean validateAlert(){
		if(tgl_email_btn.isSelected()){
			if(Util.validateEmail(alertEmail.getText().toString())!=true){
				Toast.makeText(this, getString(R.string.validate_email_invalid_msg), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(tgl_phone_btn.isSelected()){
			if(Util.validateStr(alertPhone.getText().toString())!=true){
				Toast.makeText(this, "Please Enter valid phone no", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		
		return true;
		
	}
	
	void saveAlertSettings(){
		asyncCall=new AsyncCall(this);
		asyncCall.webMethod=RequestMethod.POST;
		asyncCall.setUrl(PApp.WEB_SERVICE_URL+"setalertinfo");
		asyncCall.setMessage("Saving settings please wait...");
		asyncCall.addParam("is_app_alert", tgl_application.isSelected()?"Y":"N");
		asyncCall.addParam("is_calendar_alert", tgl_application.isSelected()?"Y":"N");
		if(tgl_email_btn.isSelected()){
			asyncCall.addParam("is_email_alert", tgl_email_btn.isSelected()?"Y":"N");
			asyncCall.addParam("alert_email",alertEmail.getText().toString());
		}else{
			asyncCall.addParam("is_email_alert", tgl_email_btn.isSelected()?"Y":"N");
		}
		if(tgl_phone_btn.isSelected()){
			asyncCall.addParam("is_phone_alert", tgl_phone_btn.isSelected()?"Y":"N");
			asyncCall.addParam("alert_email",alertPhone.getText().toString());
		}else{
			asyncCall.addParam("is_phone_alert", tgl_phone_btn.isSelected()?"Y":"N");
		}
		asyncCall.addParam("access_token", getStrPref(PApp.Pref_AccessToken));
		
		asyncCall.setAsyncCallListener(new AsyncCallListener() {

			@Override
			public void onResponseReceived(String str) {
				infoLog("save_setting info:"+ str);
				
				Gson gson=new Gson();
				LoginParser response=gson.fromJson(str, LoginParser.class);
				
				if(validateResponse(response)){
					setPref(PApp.Pref_User_is_app_alert,tgl_application.isSelected()?"Y":"N");
					setPref(PApp.Pref_User_is_calendar_alert, tgl_calender.isSelected()?"Y":"N");
					setPref(PApp.Pref_User_is_email_alert, tgl_email_btn.isSelected()?"Y":"N");
					setPref(PApp.Pref_User_is_phone_alert, tgl_phone_btn.isSelected()?"Y":"N");
					
					
					if(tgl_email_btn.isSelected()){
						setPref(PApp.Pref_User_alert_email, alertEmail.getText().toString());
					}
					if(tgl_phone_btn.isSelected()){
						setPref(PApp.Pref_User_alert_phone, alertPhone.getText().toString());
					}
				}else{
					infoLog("Login failed:"+response.errorMsg);
					showErrorMessage(response.errorMsg, "Error");
				}
			}
			
			@Override
			public void onErrorReceived(String str) {
				infoLog("Error:"+str);	
				showErrorMessage(str, "Error");
			}
			
		});
		asyncCall.execute();
		
	}
	
	
}
