package com.me.pulcer.common;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.getplusapp.mobile.android.PushNotificaionDialog_;
import com.getplusapp.mobile.android.R;

public class LocalNotifRecevier extends BroadcastReceiver {
	
	
	public static String NOTIF_RECEVED_ACTION="com.plus.intent.action.notif_recevied";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		if(action!=null && action.equalsIgnoreCase(NOTIF_RECEVED_ACTION) ){
			if(intent.getExtras()!=null){
				Bundle b=intent.getExtras();
				buildNotification(context, b.getInt("USER_ID"),b.getInt("REMINDER_ID"));
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void buildNotification(Context context,int userId,int reminderId){
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		long when = System.currentTimeMillis();  
        Notification notification = new Notification(R.drawable.plus_icon, "reminder", when);
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= notification.FLAG_AUTO_CANCEL;
        Intent notificationIntent = new Intent(context,PushNotificaionDialog_.class );
        notificationIntent.putExtra("USER_ID", userId);
        notificationIntent.putExtra("REMINDER_ID", reminderId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent , 0);
        notification.setLatestEventInfo(context, "Plus", "Balance Your Life", contentIntent);
        Random rand=new Random();
        nm.notify(rand.nextInt(), notification);
	}
	
}
