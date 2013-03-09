package com.me.pulcer.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.me.pulcer..PushNotificaionDialog_;
import com.me.pulcer..util.PLogger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class PushNotificationReceiver extends BroadcastReceiver {

	
	@Override
	public void onReceive(Context context, Intent intent) {
		infoLog("Notif_Received:"+intent.toString());
		String action = intent.getAction();
		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
            int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
            infoLog("Received push notification. Alert: " + intent.getStringExtra(PushManager.EXTRA_ALERT) + " [NotificationID="+id+"]");
            logPushExtras(intent);

        } else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
   
        	infoLog("User clicked notification. Message: " + intent.getStringExtra(PushManager.EXTRA_ALERT));
            logPushExtras(intent);
            
            if(intent.getExtras()!=null){
            	int remID=Integer.valueOf(intent.getExtras().getString("reminder_id"));
            	int userID=Integer.valueOf(intent.getExtras().getString("user_id"));
            	Intent launch = new Intent(Intent.ACTION_MAIN);
            	launch.putExtra("USER_ID", userID);
            	launch.putExtra("REMINDER_ID", remID);
                launch.setClass(UAirship.shared().getApplicationContext(), PushNotificaionDialog_.class);
                launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UAirship.shared().getApplicationContext().startActivity(launch);
            }
            
            

        } else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
          infoLog("Registration complete. APID:" + intent.getStringExtra(PushManager.EXTRA_APID)+ ". Valid: " + intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false));
        }
		
	}
	
	private void infoLog(String msg){
		PLogger.getLogger().info("PushNotificationReceiver=>"+msg);
	}
	
	private void logPushExtras(Intent intent) {
        Set<String> keys = intent.getExtras().keySet();
        for (String key : keys) {

            //ignore standard C2DM extra keys
            List<String> ignoredKeys = (List<String>)Arrays.asList(
                    "collapse_key",//c2dm collapse key
                    "from",//c2dm sender
                    PushManager.EXTRA_NOTIFICATION_ID,//int id of generated notification (ACTION_PUSH_RECEIVED only)
                    PushManager.EXTRA_PUSH_ID,//internal UA push id
                    PushManager.EXTRA_ALERT);//ignore alert
            if (ignoredKeys.contains(key)) {
                continue;
            }
            infoLog("Push Notification Extra: ["+key+" : " + intent.getStringExtra(key) + "]");
        }
    }

}
