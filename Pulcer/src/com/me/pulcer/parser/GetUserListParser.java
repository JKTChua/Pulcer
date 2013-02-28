package com.me.pulcer.parser;

import java.util.ArrayList;

import com.getplusapp.mobile.android.entity.UserDetail;
import com.google.gson.annotations.SerializedName;

public class GetUserListParser extends Response {
	
	@SerializedName("body")
	public Data data;
	
	public class Data{
		
		@SerializedName("user_list")
		public ArrayList<UserDetail> userList;
	}

}
