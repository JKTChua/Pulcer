package com.me.pulcer.parser;

import java.util.ArrayList;

import com.getplusapp.mobile.android.entity.Reminder;
import com.google.gson.annotations.SerializedName;

public class UserPillParser extends Response {
	
	@SerializedName("body")
	public Data data;
	
	
	
	public class Data{
		
		@SerializedName("medicines")
		public ArrayList<Reminder> medicine;
	}

}
