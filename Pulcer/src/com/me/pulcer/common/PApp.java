package com.me.pulcer.common;

import android.content.Intent;
import android.location.Location;
import android.os.Environment;

import com.me.pulcer.R;
import com.me.pulcer.adapter.FamilyAdapter;
import com.me.pulcer.common.LocationHelper.LocationResult;
import com.me.pulcer.util.ImageLoader;
import com.me.pulcer.util.Util;
import com.the9tcat.hadi.HadiApplication;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;


public class PApp extends HadiApplication {

	
	
	//Web Section
	public static final int CONNECTION_TIMEOUT=50000;
	public static final String  WEB_SERVICE_URL="http://dev.smruticonsulting.com/plus/";
	
	
	
	public static final String ACTION_GET_ALL_DETAIL="com.me.pulcer.get_all_detail_action";
	//Constant
	public static final String PLUS_PREFERANCE="plus_preferance";
	public static final String Pref_AccessToken="pref_access_token";
	public static final String Pref_UserID="pref_user_id";
	public static final String Pref_User_Profile_name="pref_user_profile_name";
	public static final String Pref_User_Email="pref_user_profile_Email";
	public static final String Pref_User_PWD="pref_user_profile_PWD";
	public static final String Pref_User_BDate="pref_user_profile_bdate";
	public static final String Pref_User_gender="pref_user_profile_geneder";
	public static final String Pref_User_photoUrl="pref_user_profile_photoUrl";
	public static final String Pref_User_is_app_alert="pref_User_is_app_alert";
	public static final String Pref_User_is_calendar_alert="pref_User_is_calendar_alert";
	public static final String Pref_User_is_email_alert="pref_User_is_email_alert";
	public static final String Pref_User_is_phone_alert="pref_User_is_phone_alert";
	public static final String Pref_User_alert_email="pref_User_alert_email";
	public static final String Pref_User_alert_phone="pref_User_alert_phone";
	
	public static final String Pref_Selected_User_Name="pref_selected_user";
	public static final String Pref_Selected_User_Id="pref_selected_Id";
	public static final String Pref_Sync_Date="Pref_sync_date";
	
	public static final String DIALOG_DATA="Dialog_data";
	
	public static final String BASE_DIRECTORY=Environment.getExternalStorageDirectory() + "/Plus/";
	public static final String IMAGE_DIR=BASE_DIRECTORY+"Images/";
	public static final String VIDEO_DIR=BASE_DIRECTORY+"Videos/";
	public static final String VIDEO_LAZYCACHE=BASE_DIRECTORY+"ImageCache/";
	
	
	public static final String SHAPE_CAPSULE="capsule";
	public static final String SHAPE_TABLET="round";
	
	public static final int MANAGE_USER=1001;
	public static final int SHARED_USER=1002;
	public static final int MANAGE_USER_EDIT=1003;
	public static final int REQ_ADD_PILL_SUCESSFULL=2001;
	
	public LocationHelper locationHelper;
	public Location currentLocation;
	
	public ImageLoader imageLoader;
	
//	public ArrayList<UserDetail> familyUserlist;
	//TODO this is used in UserHomeV2 Implementation
	//Remove if not using UserHomeV2
	public FamilyAdapter familyAdapter;
	public boolean isRefresUserData=true;
//	public UserDetail currentUser;
	
	@Override
	public void onCreate() {
		super.onCreate();
		AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this, options);
        PushManager.enablePush();
        CustomPushNotificationBuilder nb = new CustomPushNotificationBuilder();
        nb.statusBarIconDrawableId = R.drawable.plus_icon;//custom status bar icon
        nb.layout = R.layout.notification;
        nb.layoutIconDrawableId = R.drawable.plus_icon;//custom layout icon
        nb.layoutIconId = R.id.icon;
        nb.layoutSubjectId = R.id.subject;
        nb.layoutMessageId = R.id.message;
        PushManager.shared().setNotificationBuilder(nb);
        PushManager.shared().setIntentReceiver(PushNotificationReceiver.class);
        locationHelper=new LocationHelper();
        imageLoader = new ImageLoader(getApplicationContext());
        
        Util.createRequireDirectory();
	}
	
	public void startSyncService(){
		this.startService(new Intent(this, SyncReminderService.class));
	}
	
	//TODO Need to call this on logout/application gets close
	public void stopSyncService(){
		this.stopService(new Intent(this, SyncReminderService.class));
	}
	
	public void startChkNotifService(){
		this.startService(new Intent(this, ChkLocalNotifService.class));
	}
	public void stopChkNotifService(){
		this.stopService(new Intent(this, ChkLocalNotifService.class));
	}
	
	public Location getLocation(){
		return this.currentLocation;
	}
	
	public void startLocation(){
		if(locationHelper!=null)
			locationHelper.getLocation(getApplicationContext(), locationResult);
	}
	public void stopLocation(){
		this.locationHelper.stopLocationUpdates();
		this.currentLocation=null;
	}

	public LocationResult locationResult = new LocationResult()
	{
	    @Override
	    public void gotLocation(final Location location)
	    {
	        currentLocation = new Location(location);
	    }
	};
}
