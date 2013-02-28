package com.me.pulcer.parser;

import com.google.gson.annotations.SerializedName;

public class FBParser {
	
	
	@SerializedName("id")
	public String fbId;
	
	@SerializedName("first_name")
	public String fName="";
	
	@SerializedName("last_name")
	public String lName="";
	
	@SerializedName("username")
	public String fbUserName;
	
	@SerializedName("email")
	public String email="";
	
	@SerializedName("gender")
	public String gender="";
	
	@SerializedName("picture_url")
	public String pictureURL="";
	
	@SerializedName("birth_data")
	public String birthDate="";

	
	public String getProfileName(){
		
		return fName+" "+lName;
	}
	

}
