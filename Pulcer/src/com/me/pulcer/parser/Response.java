package com.me.pulcer.parser;

import com.google.gson.annotations.SerializedName;

public class Response {
	
	@SerializedName("statuscode")
	public int statusCode;
	
	@SerializedName("errormessage")
	public String errorMsg;
	
	
	

}
