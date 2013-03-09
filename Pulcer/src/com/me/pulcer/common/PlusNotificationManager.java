package com.me.pulcer.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.me.pulcer.entity.Reminder;
import com.me.pulcer.entity.Transaction;
import com.me.pulcer.entity.UserDetail;
import com.me.pulcer.parser.UserDetailParser;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.the9tcat.hadi.DefaultDAO;

public class PlusNotificationManager {

	Context context;
	AlarmManager alarmManager;
	SharedPreferences preferences;
	DefaultDAO dao;
	SimpleDateFormat timeFormater;
	
	public PlusNotificationManager(Context context){
		this.context=context;
		timeFormater=new SimpleDateFormat("HH:mm:ss");
		preferences=context.getSharedPreferences(PApp.PLUS_PREFERANCE,Context.MODE_PRIVATE);
		alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		dao=new DefaultDAO(context);
	}
	
	public void ScheduleNotification(Reminder reminder){
		
		try{
			if(reminder!=null){
				
				String frequency[]=reminder.days.split(",");
				if(frequency!=null){
					for(int i=0;i<frequency.length;i++){
						Calendar cal=Calendar.getInstance();
						if(getWeekDay(frequency[i])!=-1){
							Date time=timeFormater.parse(reminder.time);
							cal.set(Calendar.DAY_OF_WEEK, getWeekDay(frequency[i]));
							cal.set(Calendar.HOUR_OF_DAY, time.getHours());
							cal.set(Calendar.MINUTE, time.getMinutes());
							addAlaram(cal, reminder.reminderId, reminder.userId);
						}
					}
					addReminderToDb(reminder);
				}
			}
		}catch(Exception e){
			infoLog("Error while ScheduleNotification:"+e);
			e.printStackTrace();
		}
		
	}
	
	private void addAlaram(Calendar cal,int ReminderId,int userId){
		Date curTime=new Date();
		if(cal.getTimeInMillis()>curTime.getTime()){
			infoLog("Setting Alarm Manager: UserId:"+userId+" reminderId:"+ReminderId+" date:"+cal.getTime().toLocaleString());
			Intent actionIntent=new Intent(LocalNotifRecevier.NOTIF_RECEVED_ACTION);
			actionIntent.putExtra("USER_ID", userId);
			actionIntent.putExtra("REMINDER_ID", ReminderId);
			PendingIntent boradCatIntent=PendingIntent.getBroadcast(context, ReminderId, actionIntent, 0);
			
			long interval = 7 * AlarmManager.INTERVAL_DAY;
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, boradCatIntent);
			
		}else{
			//TODO if time is elapsed Add reminder for next week for that day
			infoLog("Reminder Time is Elapseed: userID:"+userId+" reminder:"+ReminderId+"date"+cal.getTime().toLocaleString());
			cal.add(Calendar.DATE, 7);
			infoLog("New Date next week:date"+cal.getTime().toLocaleString());
			Intent actionIntent=new Intent(LocalNotifRecevier.NOTIF_RECEVED_ACTION);
			actionIntent.putExtra("USER_ID", userId);
			actionIntent.putExtra("REMINDER_ID", ReminderId);
			PendingIntent boradCatIntent=PendingIntent.getBroadcast(context, ReminderId, actionIntent, 0);
			long interval = 7 * AlarmManager.INTERVAL_DAY;
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, boradCatIntent);
			
		}
	}
	
	public  void cancelAlarm(Calendar cal,int reminderId,int userid){
		Intent actionIntent=new Intent(LocalNotifRecevier.NOTIF_RECEVED_ACTION);
		actionIntent.putExtra("USER_ID", userid);
		actionIntent.putExtra("REMINDER_ID", reminderId);
		PendingIntent boradCatIntent=PendingIntent.getBroadcast(context, reminderId, actionIntent, 0);
		alarmManager.cancel(boradCatIntent);
	}
	
	public void addReminderToDb(Reminder reminder){
		try{
			dao.insert(reminder);
		}catch(Exception e){
			infoLog("Error while saving reminder to db");
		}
	}

	/*public void RemoveNotification(Reminder row){
		try{
			
			long deleteId=dao.delete_by_primary(row);
			infoLog("Reminder Removed SuccessFully:"+deleteId);
			
		}catch(Exception e){
			infoLog("Error while removing reminder from db"+e);
		}
	}*/
	
	public void RemoveNotification(int reminderId){
		try{
			String args[]={""+reminderId};
			@SuppressWarnings("unchecked")
			List<Reminder> list=(List<Reminder>) dao.select(Reminder.class, false, "reminder_id=", args, null, null, null, null);
			if(list!=null){
				for(Reminder rem:list){
					dao.delete_by_primary(rem);
				}
			}else{
				infoLog("RemoveNotification:->No Records Found:remId:"+reminderId);
			}
			
		}catch(Exception e){
			infoLog("Error while retriving reminder to db");
		}
	}
	
	private void infoLog(String msg){
		PLogger.getLogger().info("Plus Notif Manager:=>"+msg);
	}
	
	@SuppressWarnings("unchecked")
	public  UserDetailParser getUserData(int userId,int day){
		UserDetailParser response=null;
		try{
			infoLog("Retrving UserInfo from db userId:"+userId+" day:"+day);
			response=new UserDetailParser();
			response.init();
			
			String args[]={""+userId};
			List<UserDetail>userList=(List<UserDetail>) dao.select(UserDetail.class, false, "id=?",args,null,null,null,null);
			
			if(userList!=null && userList.size()!=0){
//				List<Reminder> temp=(List<Reminder>) dao.select(Reminder.class, false, null, null, null, null, null, null);
				/*for(Reminder rem:temp){
//					infoLog("rId:"+rem.reminderId+" Name:"+rem.medication+" days:"+rem.days);
					infoLog("rId:"+rem.reminderId+" Name:"+rem.medication+" image:"+rem.imageFile+" status:"+rem.status);
				}*/
				
				/*List<Transection> trans=(List<Transection>) dao.select(Transection.class, false, null, null, null, null, null, null);
				for(Transection tr:trans){
					infoLog("rId:"+tr.reminderId+" Name:"+tr.status);
				}*/
				
				response.data.userInfo=userList.get(0);
				
				if(userId==Util.getIntPref(context,PApp.Pref_UserID)){
					infoLog("Retriving family list:");
					String fargs[]={""+userId};
					List<UserDetail>familyList=(List<UserDetail>) dao.select(UserDetail.class, false, "id!=?",fargs,null,null,null,null);
					if(familyList!=null){
						response.data.familyList=(ArrayList<UserDetail>) familyList;
					}
				}
				
				
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				String now=df.format(new Date());
				
				String selection="RM.Medication," +
						"RM.Dosage,RM.Time," +
						"RM.pill_time," +
						"RM.Days," +
						"RM.dosage_times,"+
						"RM.reminder_id," +
						"RM.hours_between," +
						"RM.intake_id," +
						"RM.dosage_values," +
						"RM.user_id," +
						"RM.image_file," +
						"RM.transaction_date" ;
						
				
				String qryUpComming="SELECT "+selection+",RM.status"+" FROM "+Reminder.TABLE_NAME+" RM LEFT JOIN "
							+Transaction.TABLE_NAME+" PT on PT.reminder_id=RM.reminder_id AND"
							+" '"+now+"'=strftime('%Y-%m-%d',PT.tran_date) "
							+"where PT.tran_date is null AND "
							+"RM.user_id="+userId+" AND Days like '%"+day+"%'";

				Cursor c=dao.executeRawQuery(qryUpComming);
			/*	String colName[]=c.getColumnNames();
				
				for(String col:colName){
					infoLog("col:"+col);
				}*/
				response.data.upCommingList=new ArrayList<Reminder>();
				infoLog("Retriving upCommindList: ");
				if(c!=null && c.getCount()>0){
					c.moveToFirst();
					do{
						infoLog(""+c.getInt(c.getColumnIndex("reminder_id"))+" Name:"+c.getString(c.getColumnIndex("Medication"))+"status:"+c.getInt(c.getColumnIndex("status")));
						response.data.upCommingList.add(Reminder.getObjec(c,true));
					}while(c.moveToNext());
				}else{
					infoLog("Cursor is null:");
				}
				if(c!=null){
					c.close();
				}
				try{
					Reminder nextup=null;
					Date curTime=new Date();
					if(response.data.upCommingList!=null && response.data.upCommingList.size()>0){
						SimpleDateFormat timeFormater=new SimpleDateFormat("HH:mm:ss");
						nextup=response.data.upCommingList.get(0);
						for(Reminder rem:response.data.upCommingList){
							Date remTime=timeFormater.parse(rem.time);
							Date nextUpTime=timeFormater.parse(nextup.time);
							if(remTime.compareTo(nextUpTime)<0 && curTime.compareTo(remTime)<0){
								nextup=rem;
							}
						}
					}
					
					Date td=timeFormater.parse(nextup.time);
					infoLog("curTime:"+curTime.toString()+" nextUpTime:"+td.toString());
					infoLog("condition:"+td.compareTo(curTime));
					
					if(nextup!=null && td.compareTo(curTime)>0){
						infoLog("NextMedicine find:"+nextup.reminderId);
						response.data.nextMedicine=nextup;
					}
				}catch(Exception e){
					infoLog("Error while finding NextUp:"+e);
				}
				String qryCombineQry="SELECT "+selection+",PT.status"+" FROM "+Reminder.TABLE_NAME+" RM LEFT JOIN "
						+Transaction.TABLE_NAME+" PT on PT.reminder_id=RM.reminder_id AND"
						+" '"+now+"'=strftime('%Y-%m-%d',PT.tran_date) "
						+"WHERE RM.user_id="+userId+" AND PT.status<>0 AND Days like '%"+day+"%'";
				
				infoLog("Retriving combinnedList: ");
				Cursor cur=dao.executeRawQuery(qryCombineQry);
				response.data.combinnedList=new ArrayList<Reminder>();
				if(cur!=null && cur.getCount()>0){
					cur.moveToFirst();
					
					do{
						infoLog(""+cur.getInt(cur.getColumnIndex("reminder_id"))+" Name"+cur.getString(cur.getColumnIndex("Medication"))+" status:"+cur.getInt(cur.getColumnIndex("status")));
						response.data.combinnedList.add(Reminder.getObjec(cur,false));
					}while(cur.moveToNext());
				}else{
					infoLog("Cursor is null:");
				}
				if(c!=null){
					cur.close();
				}
			}else{
				infoLog("User Not Found:");
			}
		}catch(Exception e){
			infoLog("Error while retrving getUserData: userId:"+userId);
			e.printStackTrace();
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public void updateTransection(Reminder rem,int userId){
		try{
			if(rem.status!=Reminder.STATE_SCHEDULE){
				Transaction trans=new Transaction();
				trans.userId=userId;
				trans.reminderId=rem.reminderId;
				trans.tDate=rem.transactionDate;
				trans.status=rem.status;
				String selArgs[]={""+trans.reminderId,""+trans.tDate};
				List<Reminder> remList=(List<Reminder>) dao.select(Transaction.class, false, "reminder_id=? AND tran_date=?", selArgs, null, null, null, null);
				if(remList!=null && remList.size()>0){
					infoLog("Updating transection:"+rem.medication);
					dao.update_by_primary(trans);
				}else{
					infoLog("inserting new transection:"+rem.medication);
					dao.insert(trans);
				}
			}else{
				infoLog("Reminder is Schedule not processing:"+rem.medication);
			}
			
		}catch(Exception e){
			infoLog("Error while updating transection:"+e);;
		}
	}
	
	
	public int getWeekDay(String str){
		int ret=-1;
		try{
			if(str!=null && str.length()>0){
				if(str.equalsIgnoreCase("7")){
					ret=1;
				}else{
					ret=Integer.valueOf(str);
					if(ret>0 && ret <7){
						ret=ret+1;
					}
				}
			}
		}catch(Exception e){
			
		}
		return ret;
	}
	public int getWeekDayToPlus(String str){
		
		int ret=-1;
		try{
			if(str!=null && str.length()>0){
				if(str.equalsIgnoreCase("1")){
					ret=7;
				}else{
					ret=Integer.valueOf(str);
					if(ret>0 && ret <7){
						ret=ret-1;
					}
				}
			}
		}catch(Exception e){
			
		}
		return ret;
		
	}
	public void chkNotification(Date time){
		
	}
}
