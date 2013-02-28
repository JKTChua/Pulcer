package com.me.pulcer.parser;

import com.getplusapp.mobile.android.entity.Reminder;
import com.google.gson.annotations.SerializedName;

public class NotificationParser extends Response {
	
	@SerializedName("body")
	public Data data;

	
	public class Data{
		
		@SerializedName("reminder_detail")
		public Reminder reminderDetail;
		
	}
}
