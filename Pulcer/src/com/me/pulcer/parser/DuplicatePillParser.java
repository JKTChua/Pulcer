package com.me.pulcer.parser;

import com.me.pulcer.entity.Reminder;
import com.google.gson.annotations.SerializedName;

public class DuplicatePillParser extends Response {
	
	@SerializedName("body")
	public Reminder remininder;

}
