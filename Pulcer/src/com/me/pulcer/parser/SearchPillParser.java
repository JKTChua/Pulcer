package com.me.pulcer.parser;

import java.util.ArrayList;

import com.getplusapp.mobile.android.entity.Pill;
import com.google.gson.annotations.SerializedName;

public class SearchPillParser extends Response {
	
	@SerializedName("body")
	public ArrayList<Pill> pillList=new ArrayList<Pill>();
	

}
