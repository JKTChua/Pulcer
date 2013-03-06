package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.me.pulcer.util.PLogger;
import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="Notification_Reminders")
public class Reminder implements Serializable{

	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7184151187345715718L;
	public static final int STATE_SCHEDULE=0;
	public static final int STATE_TAKEN=1;
	public static final int STATE_MISSED=2;
	public static final int STATE_SKIPPED=3;
	
	public static String TABLE_NAME="Notification_Reminders";
	
	@Column(name="Medication")
	@SerializedName("Medication")
	public String medication;
	
	@Column(name="Dosage")
	@SerializedName("Dosage")
	public String dosage;
	
	@Column(name="Time")
	@SerializedName("Time")
	public String time;
	
	@Column(name="pill_time")
	@SerializedName("pill_time")
	public String pillTime;
	
	@Column(name="Days")
	@SerializedName("Days")
	public String days;
	
	@Column(name="dosage_times")
	@SerializedName("dosage_times")
	public int dosageTimes;
	
	//TODO Need TO check this Reminder id is not unique
	@Column(name="reminder_id",primary=true)
	@SerializedName("reminder_id")
	public int reminderId;
	
	@Column(name="hours_between")
	@SerializedName("hours_between")
	public int hoursBetween;
	
	@Column(name="intake_id")
	@SerializedName("intake_id")
	public int intakeId;
	
	@Column(name="dosage_values")
	@SerializedName("dosage_values")
	public String dosageValuesList;
	
	@Column(name="user_id")
	@SerializedName("user_id")
	public int userId;
	
	//TODO Need to check this
	@Column(name="image_file")
	@SerializedName("image_file")
	public String imageFile;
	
	@Column(name="transaction_date")
	@SerializedName("transaction_date")
	public String transactionDate="0000-00-00 00:00:00";
	
	@Column(name="status")
	@SerializedName("status")
	public int status;

	public boolean isToDisable;
	public boolean isOpen;
	public boolean isToAnimateOpen=false;
	public boolean isToAnimateClose=false;
	
	
	
	public static Reminder getObjec(Cursor c,boolean isUpComming){
		Reminder object=null;
 		try{
			if(c!=null){
				object=new Reminder();
				object.medication=c.getString(c.getColumnIndex("Medication"));
				object.dosage=c.getString(c.getColumnIndex("Dosage"));
				object.time=c.getString(c.getColumnIndex("Time"));
				object.pillTime=c.getString(c.getColumnIndex("pill_time"));
				object.days=c.getString(c.getColumnIndex("Days"));
				object.reminderId=c.getInt(c.getColumnIndex("reminder_id"));
				object.hoursBetween=c.getInt(c.getColumnIndex("hours_between"));
				object.dosageTimes=c.getInt(c.getColumnIndex("dosage_times"));
				object.imageFile=c.getString(c.getColumnIndex("image_file"));
				object.dosageValuesList=c.getString(c.getColumnIndex("dosage_values"));
				object.transactionDate=c.getString(c.getColumnIndex("transaction_date"));
				object.intakeId=c.getInt(c.getColumnIndex("intake_id"));
				object.userId=c.getInt(c.getColumnIndex("user_id"));
				if(isUpComming){
					object.status=0;
				}else{
					object.status=c.getInt(c.getColumnIndex("status"));
				}
				
				
			}
		}catch(Exception e){
			PLogger.getLogger().info("Error while building list"+e);
		}
 		return object;
	}
	
	
}
