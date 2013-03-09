package com.me.pulcer.parser;

import java.util.ArrayList;

import com.me.pulcer.entity.Reminder;
import com.google.gson.annotations.SerializedName;

public class SyncServerParser extends Response {
	
	@SerializedName("body")
	public Data data;
	
	
	
	public class Data{
		
		@SerializedName("sync_date")
		public String syncDate;
		
		@SerializedName("new_schedule")
		public ArrayList<DaySchedule> NewReminderList;

		/*@SerializedName("deleted_schedule")
		DaySchedule DeleteSchedule;*/
		
	}

	public class DaySchedule{
		
		@SerializedName("date")
		public String date;
		
		@SerializedName("reminders")
		public ArrayList<Reminder> scheduleList;
		
		
	}
	
}
