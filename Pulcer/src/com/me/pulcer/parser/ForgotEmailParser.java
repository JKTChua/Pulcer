package com.me.pulcer.parser;

import com.google.gson.annotations.SerializedName;

public class ForgotEmailParser extends Response {
	
	@SerializedName("body")
	public Data data;
	
	
	public class Data{
		
		@SerializedName("message")
		public String message;
	}

	
	

}
