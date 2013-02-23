package com.me.pulcer.parser;

import com.google.gson.annotations.SerializedName;

public class LoginParser extends Response {
	
	@SerializedName("body")
	public LoginData data;

	public class LoginData{
		
		@SerializedName("id")
		public int userID;
		
		@SerializedName("email")
		public String email;
		
		@SerializedName("profile_name")
		public String profileName;
		
		@SerializedName("birth_date")
		public String birthData;
		
		@SerializedName("gender")
		public String gender;
		
		@SerializedName("photo_url")
		public String photoUrl;
		
		@SerializedName("is_app_alert")
		public String isAppAlert;
		
		@SerializedName("is_calendar_alert	")
		public String isCalenderAlert;
		
		
		@SerializedName("is_email_alert")
		public String isEmailAlert;
		
		@SerializedName("is_phone_alert")
		public String isPhoneAlert;
		
		
		@SerializedName("alert_email")
		public String alertEmail;
		
		@SerializedName("alert_phone")
		public String alertPhone;
		
		
		
		@SerializedName("access_token")
		public String accessToken;
		
		@SerializedName("operation")
		public String operation;
		
		
		
		
		
	}
}

