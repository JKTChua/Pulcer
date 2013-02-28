package com.me.pulcer.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Pill implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	public int pillId;
	
	@SerializedName("medication_name")
	public String pillName;
	
	@SerializedName("shape")
	public String pillShape;
	
	@SerializedName("dosage")
	public String dosage;
	
	@SerializedName("dosage_values")
	public String dosage_values;
	
	public boolean isDummy=false;
	public String [] getDosageValueList(){
		String []list=dosage_values.split(",");
		return list;
	}
}
