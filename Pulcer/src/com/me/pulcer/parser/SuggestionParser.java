package com.me.pulcer.parser;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SuggestionParser extends Response{
	
	@SerializedName("body")
	public SuggestionData data;
	
	public class SuggestionData{
		
		@SerializedName("suggested_list")
		public ArrayList<String> listArray;
		
		
	}

}
