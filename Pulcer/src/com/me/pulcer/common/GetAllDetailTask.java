package com.me.pulcer.common;

import java.util.ArrayList;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.me.pulcer.entity.Reminder;
import com.me.pulcer.entity.UserDetail;
import com.me.pulcer.parser.GetAllDetailParser;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.HttpUtil;
import com.me.pulcer.web.RequestMethod;
import com.google.gson.Gson;
import com.the9tcat.hadi.DefaultDAO;

public class GetAllDetailTask extends TimerTask {

	private PlusNotificationManager plusNotifManager;
	private ArrayList<NameValuePair> params;
	private HttpUtil httpUtill;
	private Context context;
	
	public GetAllDetailTask(Context context){
		params=new ArrayList<NameValuePair>();
		httpUtill=new HttpUtil();
		this.context=context;
		this.plusNotifManager=new PlusNotificationManager(context);
	}
	
	@Override
	public void run() {
		params.clear();
		params.add(new BasicNameValuePair("access_token", Util.getStrPref(context,PApp.Pref_AccessToken)));
		try {
			String response=httpUtill.execute(RequestMethod.GET,params, PApp.WEB_SERVICE_URL+"getalldetails");
			Gson gson=new Gson();
			processResp(gson.fromJson(response, GetAllDetailParser.class));
		}catch (Exception e) {
			infoLog("Error on Sync:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void processResp(GetAllDetailParser resp){
		try{
			DefaultDAO dao=new DefaultDAO(context);
			String []args={"0"};
			dao.delete(UserDetail.class, "id>?",args);
			if(resp!=null && resp.data!=null && resp.data.userList!=null){
				int cnt=0;
				for(UserDetail user:resp.data.userList){
					dao.insert(user);
					cnt++;
				}
				/*for(UserDetail user:resp.data.userList){
					addReminderToDb(user);
				}*/
				infoLog("GetAllDetail No rows Inserted:"+cnt);
			}else{
				infoLog("GetAllUSerdetail respons/data is null");
			}
		}catch(Exception e){
			infoLog("Error while parsing getallDetail resp:"+e);
		}
	}
	
	private void infoLog(String message){
		PLogger.getLogger().info("GetAllDetail=>"+message);
	}
	private void addReminderToDb(UserDetail user){
		try{
			infoLog("User:"+user.user_name+" userID:"+user.id);
			if(user.upCommingList!=null){
				infoLog("User:"+user.user_name+" upCommind:"+user.upCommingList.size());
				for(Reminder rem:user.upCommingList){
					rem.userId=user.id;
					rem.status=Reminder.STATE_SCHEDULE;
					plusNotifManager.addReminderToDb(rem);
				
				}
			}
			
			if(user.takenList!=null){
				infoLog("User:"+user.user_name+" taken:"+user.takenList.size());
				for(Reminder rem:user.takenList){
					rem.status=Reminder.STATE_TAKEN;
					rem.userId=user.id;
					plusNotifManager.addReminderToDb(rem);
				}
			}
			
			if(user.missedList!=null){
				infoLog("User:"+user.user_name+" missed:"+user.missedList.size());
				for(Reminder rem:user.missedList){
					rem.status=Reminder.STATE_MISSED;
					rem.userId=user.id;
					plusNotifManager.addReminderToDb(rem);
				}
			}
			
		}catch(Exception e){
			infoLog("Error while adding pill to db:"+e);
		}
	}
	
	
}
