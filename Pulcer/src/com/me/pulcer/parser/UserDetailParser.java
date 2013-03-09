package com.me.pulcer.parser;

import java.util.ArrayList;

import com.me.pulcer.entity.Reminder;
import com.me.pulcer.entity.UserDetail;
import com.google.gson.annotations.SerializedName;


public class UserDetailParser extends Response  {

	
	/**
	 * 
	 */
	@SerializedName("body")
	public UserData data;
	
	public class UserData{
		
		@SerializedName("user_info")
		public UserDetail userInfo;
		
		@SerializedName("next")
		public Reminder nextMedicine;
		
		@SerializedName("upcoming")
		public ArrayList<Reminder> upCommingList;
		
		/*@SerializedName("taken")
		public ArrayList<Reminder> takenList;*/
		
		/*@SerializedName("missed")
		public ArrayList<Reminder> missedList;*/
		
		@SerializedName("family_info")
		public ArrayList<UserDetail> familyList;
		
		@SerializedName("statistics")
		public statistics state;
		
		@SerializedName("combined")
		public ArrayList<Reminder> combinnedList;
		
		
		
	}
	public class statistics{
		
		@SerializedName("you")
		public int you;
		
		@SerializedName("age")
		public int age;
		
		@SerializedName("plus")
		public int plus;
		
		@SerializedName("national")
		public int national;
	}
	
	public void init(){
		data=new UserData();
		data.state=new statistics();
	}
	
}
