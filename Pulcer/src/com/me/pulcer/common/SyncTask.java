package com.me.pulcer.common;

import java.util.ArrayList;
import java.util.TimerTask;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.content.Context;
import android.content.SharedPreferences;
import com.me.pulcer.entity.Reminder;
import com.me.pulcer.parser.SyncServerParser;
import com.me.pulcer.parser.SyncServerParser.DaySchedule;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.web.HttpUtil;
import com.me.pulcer.web.RequestMethod;
import com.google.gson.Gson;

public class SyncTask extends TimerTask{
	
	private ArrayList<NameValuePair> params;
	private HttpUtil httpUtil;
//	private Context context;
	private SharedPreferences preferences;
	private PlusNotificationManager plusNotifManager;
	
	public SyncTask(Context context){
		params=new ArrayList<NameValuePair>();
//		this.context=context;
		httpUtil=new HttpUtil();
		plusNotifManager=new PlusNotificationManager(context);
		preferences=context.getSharedPreferences(PApp.PLUS_PREFERANCE, Context.MODE_PRIVATE);
	}
	
	@Override
	public void run() {
		
		params.clear();
		params.add(new BasicNameValuePair("access_token", getStrPref(PApp.Pref_AccessToken)));
		params.add(new BasicNameValuePair("user_id", ""+getIntPref(PApp.Pref_UserID)));
		params.add(new BasicNameValuePair("sync_date", getSyncDate(PApp.Pref_Sync_Date)));
		try {
			Gson gson=new Gson();
			String response=httpUtil.execute(RequestMethod.GET,params, PApp.WEB_SERVICE_URL+"syncserver");
			processResponse(gson.fromJson(response, SyncServerParser.class));
			
		}catch (Exception e) {
			infoLog("Error on Sync:"+e.getMessage());
			e.printStackTrace();
		}
	}
	private void processResponse(SyncServerParser resp){
		
		if(resp!=null && resp.data!=null){
			setPref(PApp.Pref_Sync_Date, resp.data.syncDate);
			processNewSchedule(resp.data.NewReminderList);
			
		}else{
			infoLog("Data is NULLLLLL");
		}
		
	}
	private void processNewSchedule(ArrayList<DaySchedule> NewReminderList){
		try{
			if(NewReminderList!=null && NewReminderList.size()>0){
				for(DaySchedule obj:NewReminderList){
					if(obj.scheduleList!=null){
						for(Reminder rem:obj.scheduleList){
							plusNotifManager.ScheduleNotification(rem);
						}
					}
				}
			}
		}catch(Exception e){
			infoLog("Error while process new schedule:"+e);
		}
	}
	
	private void infoLog(String message){
		PLogger.getLogger().info("SyncTask:=>"+message);
	}
	private String getStrPref(String key){
    	return preferences.getString(key, "");
    }
    
    private int getIntPref(String key){
    	return preferences.getInt(key, 0);
    }
	
    private String getSyncDate(String key){
    	return preferences.getString(key, "0000-00-00 00:00:00");
    }
    
    private void setPref(String key,String value){
    	SharedPreferences.Editor edit=preferences.edit();
    	edit.putString(key, value);
    	edit.commit();
    }
	
}
