package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;


@Table(name="USER_DETAIL")
public class UserDetail implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 21858193414754958L;

	

	@Column(name="id",primary=true)
	@SerializedName("id")
	public int id;
	
	@Column(name="")
	@SerializedName("profile_name")
	public String user_name;
	
	
	@Column(name="photo_url")
	@SerializedName("photo_url")
	public String photoUrl;
	
	@Column(name="medicines")
	@SerializedName("medicines")
	public int userMeds;
	
	@Column(name="medicine")
	@SerializedName("medicine")
	public int medicineCount;
	
	@Column(name="operation")
	@SerializedName("operation")//TOOD this field is used in add USER
	public String operation;
	
	@Column(name="birth_date")
	@SerializedName("birth_date")
	public String bDate;
	
	@Column(name="managed_by")
	@SerializedName("managed_by")
	public int manageById;
	
	@Column(name="syou")
	@SerializedName("syou")
	public int state_you;
	
	@Column(name="sage")
	@SerializedName("sage")
	public int state_age;
	
	@Column(name="splus")
	@SerializedName("splus")
	public int state_plus;
	
	@Column(name="snational")
	@SerializedName("snational")
	public int state_national;
	
	@SerializedName("upcoming")
	public ArrayList<Reminder> upCommingList;
	
	@SerializedName("taken")
	public ArrayList<Reminder> takenList;
	
	@SerializedName("missed")
	public ArrayList<Reminder> missedList;
	
	public boolean isSelected;
	public boolean isOpen;
	public boolean isToAnimateOpen=false;
	public boolean isToAnimateClose=false;
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
