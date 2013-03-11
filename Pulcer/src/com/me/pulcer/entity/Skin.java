package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;


public class Skin implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SerializedName("bid")
	@Column(primary=true,name="braden_id")
	public int bradenId;

	/**
	0 normal for ethnic group
	1 ashen
	2 cyanotic
	3 flushed
	4 jaundiced
	5 mottled
	6 pale
	 */
	@SerializedName("color")
	@Column(name="color")
	public byte color;
	
	/**
	0 cool
	1 warm (normal)
	2 cold
	3 hot
	 */
	@SerializedName("temperature")
	@Column(name="temperature")
	public byte temperature;
	
	/**
	0 normal
	1 extremely dry
	2 moist
	3 diaphoretic
	4 clammy
	 */
	@SerializedName("moistness")
	@Column(name="moistness")
	public byte moistness;

	/**
	0 good elasticity (normal)
	1 poor, decreased elasticity
	2 tenting
	 */
	@SerializedName("elasticity")
	@Column(name="elasticity")
	public byte elasticity;
}
