package com.me.pulcer.entity;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;


@Table(name="update_status_log")
public class UpdateStatus {

	@SerializedName("reminder_id")
	@Column(name="reminder_id",primary=true)
	public int reminderId;
	
	@SerializedName("user_id")
	@Column(name="user_id")
	public int userId;
	
	@SerializedName("date")
	@Column(name="date_stamp")
	public long dateStamp;
	
	@SerializedName("status")
	@Column(name="status")
	public String status;
	
	@SerializedName("lat")
	@Column(name="lat")
	public double lat;
	
	@SerializedName("lng")
	@Column(name="lng")
	public double lng;
}
