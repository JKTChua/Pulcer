package com.me.pulcer.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.getplusapp.mobile.android.entity.Reminder;
import com.getplusapp.mobile.android.util.PLogger;
import com.getplusapp.mobile.android.util.Util;
import com.the9tcat.hadi.DefaultDAO;

public class ChkLocalNotifService extends Service{

	private static final int INTERVAL=(1000*15);
	Timer timmer;
	 PlusNotificationManager notifManager;
	 DefaultDAO dao;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		timmer=new Timer();
		notifManager=new PlusNotificationManager(getApplicationContext());
		dao=new DefaultDAO(getApplicationContext());
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		infoLog("CHkNotif Start command received");
		timmer.scheduleAtFixedRate(new ChkNoitif(getApplicationContext()), 0, INTERVAL);
	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timmer.cancel();
	}
	
	private class ChkNoitif extends TimerTask{

		Context context;
		public ChkNoitif(Context context){
			this.context=context;
			
		}
		@Override
		public void run() {
			
			if(Util.chkInternet(context)){
				getData();
			}else{
				infoLog("Offline mode");
			}
		}
		public void getData(){
			Calendar cal=Calendar.getInstance();
			try{
				SimpleDateFormat df=new SimpleDateFormat("HH:mm:ss");
				String frmTime=df.format(cal.getTime());
				cal.add(Calendar.MINUTE, 1);
				String toTime=df.format(cal.getTime());
				String args[]={frmTime,toTime};
				String selection="Time BETWEEN ? AND ?";
				infoLog("condition:frmTIme:"+frmTime+"  toTime:"+toTime);
				@SuppressWarnings("unchecked")
				List<Reminder> list=(List<Reminder>) dao.select(Reminder.class, true, selection, args, null, null, null, null);
				
				if(list!=null){
					infoLog("Records Found:"+list.size());
					int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
					for(Reminder row:list){
						if(dayOfWeek==1){
							if(row.days.contains("7")){
								infoLog("Sunday Entry found:"+row.medication+" time:"+row.time+" row.days:"+row.days);
								Calendar tempCal=Calendar.getInstance();
								Date time=df.parse(row.time);
								cal.set(Calendar.DAY_OF_WEEK,1);
								cal.set(Calendar.HOUR_OF_DAY, time.getHours());
								cal.set(Calendar.MINUTE, time.getMinutes());
								infoLog("Canceling Alarm:"+tempCal.getTime().toLocaleString());
								notifManager.cancelAlarm(tempCal, row.reminderId, row.reminderId);
								
							}
						}else{
							int day=dayOfWeek-1;
							if(row.days.contains(""+day)){
//								infoLog("Other day Entry found:day:"+day+" "+row.medication+" time:"+row.time+" row.days:"+row.days);
								Calendar tempCal=Calendar.getInstance();
								Date time=df.parse(row.time);
								cal.set(Calendar.DAY_OF_WEEK,day);
								cal.set(Calendar.HOUR_OF_DAY, time.getHours());
								cal.set(Calendar.MINUTE, time.getMinutes());
								infoLog("Canceling Alarm:"+tempCal.getTime().toLocaleString());
								notifManager.cancelAlarm(tempCal, row.reminderId, row.reminderId);
							}
						}
						
					}
				}else{
					infoLog("NO Records Found:");
				}
				
				/*String tempArgs[]={"0"};
				List<Reminder> temp=(List<Reminder>) dao.select(Reminder.class, true, "reminder_id>?", tempArgs, null, null, null, null);
				if(temp!=null){
					for(Reminder data:temp){
						infoLog("Record:"+data.medication+" time:"+data.time+" data.day:"+data.days);
					}
				}
				*/
				
				
			}catch(Exception e){
				infoLog("Error while Retrving Notification"+e);
			}
		}
		
		
		
		
	}
	private void infoLog(String message){
    	PLogger.getLogger().info("ChkLocalNotifService:=>"+message);
    }
	

}
