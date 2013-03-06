package com.getplusapp.mobile.android.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewAnimator;

import com.getplusapp.mobile.android.PillList_;
import com.getplusapp.mobile.android.R;
import com.getplusapp.mobile.android.UserHome;
import com.getplusapp.mobile.android.UserList_;

public class SlideMenu{

	
	private static final int DURATION=200;
	public boolean isMenuOut=false;
	
	private FrameLayout root;
	private RelativeLayout menuRoot;
	public View contentPan;
	private int margin=75;//onIntialize value is override
	private ViewAnimator animator;
	private PButton menuOutBtn;
	Context context;
	public AnimationListener custAnim;
	LockedScrollView menu_scroll;
	
	
	ImageButton settingBtn;
	Activity activity;
	
	public SlideMenu(Activity actvity) {
		this.activity=actvity;
		this.context=actvity.getApplicationContext();
	}
	
	public void setMenuOutBtn(PButton btn){
		this.menuOutBtn=btn;
		menuOutBtn.setSelected(false);
		menuOutBtn.setOnClickListener(triggerAnim);
	}
	
	public void initializeMenu(View contentPan,RelativeLayout menuRoot,FrameLayout root){
		this.menu_scroll=(LockedScrollView) root.findViewById(R.id.menu_scroll);
		this.menu_scroll.findViewById(R.id.setting_back).setOnClickListener(settingBtnListener);
		this.menu_scroll.findViewById(R.id.menu_plus_btn).setOnClickListener(settingBtnListener);
		this.menu_scroll.findViewById(R.id.setting_adduser_btn).setOnClickListener(settingBtnListener);
		this.settingBtn=(ImageButton) root.findViewById(R.id.menu_setting_ibtn);
		this.settingBtn.setOnClickListener(settingBtnListener);
		
		this.contentPan=contentPan;
		this.menuRoot=(RelativeLayout)menuRoot;
		this.animator=(ViewAnimator) menuRoot.findViewById(R.id.menu_animator);
		this.root=root;
		margin=menuOutBtn.getMeasuredWidth()+15;
		RelativeLayout.LayoutParams param=  (LayoutParams) animator.getLayoutParams();
		param.width=contentPan.getMeasuredWidth()-margin;
		animator.setLayoutParams(param);
		
	}
	
	View.OnClickListener settingBtnListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			Animation slideIn=AnimationUtils.loadAnimation(context, R.anim.slide_in);
			Animation slideOut=AnimationUtils.loadAnimation(context, R.anim.slide_out);
			Animation slideStatic=AnimationUtils.loadAnimation(context, R.anim.static_anim);
			
			if(view.getId()==R.id.menu_setting_ibtn){
				
				animator.setInAnimation(slideOut);
				animator.setOutAnimation(slideStatic);
				animator.showNext();
			}else if(view.getId()==R.id.setting_back){
				
				animator.setInAnimation(slideStatic);
				animator.setOutAnimation(slideIn);
				animator.showPrevious();
			}else if(view.getId()==R.id.menu_plus_btn){
				Intent intent=new Intent(context, PillList_.class);
				context.startActivity(intent);
					
			}else if(view.getId()==R.id.setting_adduser_btn){
				Intent intent=new Intent(context, UserList_.class);
				activity.startActivityForResult(intent, UserHome.USER_LIST_REQUEST);
			}
			
			
		}
	};
	
	View.OnClickListener triggerAnim=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//contentPan.buildDrawingCache();
			contentPan.post(new Runnable() {
				
				@Override
				public void run() {
					root.post(new Runnable() {
						
						@Override
						public void run() {
							doAnim();
						}
					});
				}
			});
		}
	};
	
	public void doAnim(){
		TranslateAnimation animation;
		if(isMenuOut){
			
			animation=new TranslateAnimation(0, -contentPan.getLeft(), 0, 0);
			animation.setDuration(150);
			
		}else{
			
			animation=new TranslateAnimation(0, contentPan.getMeasuredWidth()-margin, 0, 0);
			animation.setDuration(DURATION);
			
		}
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		//animation.setDuration(DURATION);
		animation.setFillAfter(true);
		animation.setAnimationListener(custAnim);
		contentPan.setAnimation(animation);
		contentPan.startAnimation(animation);
	}
	
	AnimationListener animListener=new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			
			contentPan.clearAnimation();
			isMenuOut=!isMenuOut;
			setPosition();
			
		}
	};
	
	public void closeStatic(){
		this.isMenuOut=false;
		setPosition();
	}
	
	public void setPosition(){
		

		if(isMenuOut){
			contentPan.clearAnimation();
			int oldHeight=contentPan.getMeasuredHeight();
			int oldWidth=contentPan.getMeasuredWidth();
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(oldWidth,oldHeight);
			params.addRule(RelativeLayout.RIGHT_OF, animator.getId());
			root.removeView(contentPan);
			menuRoot.addView(contentPan,params);
			menuOutBtn.setSelected(true);
		}else{
			contentPan.clearAnimation();
			menuRoot.removeView(contentPan);
			root.addView(contentPan);
			menuOutBtn.setSelected(false);
			
			
		}
		
	}
	
	
}
