package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class Skin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("color")
	public byte color;
	
	@SerializedName("temperature")
	public byte temperature;
	
	@SerializedName("moistness")
	public byte moistness;

	@SerializedName("elasticity")
	public byte elasticity;
}
