package com.me.pulcer.parser;

import com.google.gson.annotations.SerializedName;

public class ReminderStatusParser extends Response {

	@SerializedName("body")
	public UpdateStatus data;
	
	
	public class UpdateStatus{
		
		@SerializedName("operation")
		public String operation;
	}
}
