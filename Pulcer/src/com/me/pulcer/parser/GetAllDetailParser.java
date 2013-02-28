package com.me.pulcer.parser;

import java.util.ArrayList;

import com.getplusapp.mobile.android.entity.UserDetail;
import com.google.gson.annotations.SerializedName;

public class GetAllDetailParser extends Response{
	
	@SerializedName("body")
	public Data data;
	
	
	public class Data{
		
		@SerializedName("user_info")
		public ArrayList<UserDetail> userList;
	}

}
