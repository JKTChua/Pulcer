package com.me.pulcer.parser;



import com.google.gson.annotations.SerializedName;


public class AddSUserParser extends Response {

	
	@SerializedName("body")
	public InviteStae data;
	
	
	public class InviteStae{
		
		@SerializedName("email")
		public String email;
		
		@SerializedName("operation")
		public String operation;
	}
	
	
	
}
