package com.me.pulcer.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class UlcerEnt implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public int ulcerId;
	
	@SerializedName("location")
	public String location;
	
	@SerializedName("stage")
	public int stage;
}
