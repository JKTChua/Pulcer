package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="braden")
public class Braden implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("bid")
	@Column(primary=true,autoincrement=true,name="braden_id")
	public int bradenId;
	
	@SerializedName("user_id")
	@Column(primary=true,name="user_id")
	public int userId;
	
	/**
	Completely Limited = 1
	Very Limited = 2
	Slightly Limited = 3
	No Impairment = 4
	 */
	@SerializedName("sensory_perception")
	@Column(name="sensory_perception")
	public int sensoryPerception;
	
	/**
	Constantly Moist = 1
	Very Moist = 2
	Occasionally Moist = 3
	Rarely Moist = 4
	 */
	@SerializedName("moisture")
	@Column(name="moisture")
	public int moisture;
	
	/**
	Bedfast = 1
	Chairfast = 2
	Walks Occasionally = 3
	Walks Frequently = 4
	 */
	@SerializedName("activity")
	@Column(name="activity")
	public int activity;

	/**
	Completely Immobile = 1
	Very Limited = 2
	Slightly Limited = 3
	No Limitation = 4
	 */
	@SerializedName("mobility")
	@Column(name="mobility")
	public int mobility;
	
	/**
	Very Poor = 1
	Inadequate = 2
	Adequate = 3
	Excellent = 4
	 */
	@SerializedName("nutrition")
	@Column(name="nutrition")
	public int nutrition;
	
	/**
	Significant Problem = 1
	Problem = 2
	Potential Problem = 3
	No Apparent Problem = 4
	 */
	@SerializedName("friction")
	@Column(name="friction")
	public int friction;
	
	/**
	Extremely Compromised = 1
	Compromised = 2
	Adequate = 3
	Excellent = 4
	 */
	@SerializedName("oxygenation")
	@Column(name="oxygenation")
	public int oxygenation;
	
	/**
	No Risk = 19 or higher
	At Risk = 15-18
	Moderate Risk = 13-14
	High Risk = 10-12
	Severe Risk = 9 or Below
	*/
	@SerializedName("risk_total")
	@Column(name="risk_total")
	public int riskTotal;
	
	
	/**
	yyyy-MM-dd HH:mm:ss
	 */
	@SerializedName("date")
	@Column(name="date")
	public String date;
}
