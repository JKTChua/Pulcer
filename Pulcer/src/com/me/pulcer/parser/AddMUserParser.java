package com.me.pulcer.parser;



import com.getplusapp.mobile.android.entity.UserDetail;
import com.google.gson.annotations.SerializedName;


public class AddMUserParser extends Response {

	
	@SerializedName("body")
	public UserDetail data;
	
	
	
}
