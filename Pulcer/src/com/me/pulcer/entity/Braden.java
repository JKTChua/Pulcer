package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class Braden implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("sensory_perception")
	public byte sensoryPerception;
	
	@SerializedName("moisture")
	public byte moisture;
	
	@SerializedName("activity")
	public byte activity;

	@SerializedName("mobility")
	public byte mobility;
	
	@SerializedName("nutrition")
	public byte nutrition;
	
	@SerializedName("friction")
	public byte friction;
	
	@SerializedName("oxygenation")
	public byte oxygenation;
	
	@SerializedName("risk_total")
	public byte riskTotal;
}
