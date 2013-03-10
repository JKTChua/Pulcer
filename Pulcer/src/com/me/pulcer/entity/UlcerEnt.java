package com.me.pulcer.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="ulcerent")
public class UlcerEnt implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("uid")
	@Column(primary=true,name="ulcer_id")
	public int ulcerId;
	
	@SerializedName("gid")
	@Column(primary=true,name="group_id")
	public int groupId;
	
	/**
	1 
	2
	3
	4
	5 unstageable
	*/
	@SerializedName("stage")
	@Column(name="stage")
	public byte stage;
	
	/**
	0 left
	1 right
	2 upper
	*/
	@SerializedName("healing_status")
	@Column(name="healing_status")
	public byte healingStatus;
	
	/**
	0 bone
	1 fascia
	2 joint capsule
	3 prosthesis
	4 pin
	5 subcutaneous tissue
	6 tendon
	7 muscle
	*/
	@SerializedName("internal")
	@Column(name="internal")
	public byte internal;
	
	@SerializedName("image")
	@Column(name="image")
	public String image;
}
