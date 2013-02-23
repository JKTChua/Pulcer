package com.me.pulcer.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
import android.content.Context;
import com.getplusapp.mobile.android.entity.Reminder;
import com.getplusapp.mobile.android.util.PLogger;
import com.getplusapp.mobile.android.util.Util;
import com.the9tcat.hadi.DefaultDAO;

public class ChkLocalNotifTask extends TimerTask {
	private Context context;
	private PlusNotificationManager notifManager;
	private DefaultDAO dao;
	
	public ChkLocalNotifTask(Context context,DefaultDAO dao){
		this.context=context;
		notifManager=new PlusNotificationManager(context);
		this.dao=dao;
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
//			infoLog("condition:frmTIme:"+frmTime+"  toTime:"+toTime);
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
//							infoLog("Other day Entry found:day:"+day+" "+row.medication+" time:"+row.time+" row.days:"+row.days);
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
			
		}catch(Exception e){
			infoLog("Error while Retrving Notification"+e);
		}
	}
	private void infoLog(String message){
    	PLogger.getLogger().info("ChkLocalNotifTask:=>"+message);
    }

}

/*String tempArgs[]={"0"};
List<Reminder> temp=(List<Reminder>) dao.select(Reminder.class, true, "reminder_id>?", tempArgs, null, null, null, null);
if(temp!=null){
	for(Reminder data:temp){
		infoLog("Record:"+data.medication+" time:"+data.time+" data.day:"+data.days);
	}
}
*/
