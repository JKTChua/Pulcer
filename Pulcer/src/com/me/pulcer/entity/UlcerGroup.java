package com.me.pulcer.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="ulcergroup")
public class UlcerGroup implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("gid")
	@Column(primary=true,autoincrement=true,name="group_id")
	public int groupId;
	
	@SerializedName("user_id")
	@Column(primary=true,name="user_id")
	public int userId;
	
	/**
	0 back of head
	1 chin
	2 shoulder
	3 scapula
	4 elbow
	5 sacrum/coccyx
	6 thoracic spine
	7 lumbar spine
	8 trochanter
	9 iliac crest
	10 buttock
	11 knee
	12 heel
	13 ankle
	14 ear
	15 cheek
	16 nose
	17 nostril
	*/
	@SerializedName("location")
	@Column(name="location")
	public int location;
	
	/**
	1 
	2
	3
	4
	5 unstageable
	*/
	@SerializedName("stage")
	@Column(name="stage")
	public int stage;
	
	/**
	false device-related
	true pressure point-related
	*/
	@SerializedName("association")
	@Column(name="association")
	public boolean association;
	
	/**
	0 left
	1 right
	2 upper
	3 lower
	4 mid
	5 anterior
	6 posterior
	7 proximal
	8 distal
	9 medial
	10 lateral 
	11 superior 
	12 inferior 
	*/
	@SerializedName("location_qualifier")
	@Column(name="location_qualifier")
	public int locationQualifier;
	
	@SerializedName("image")
	@Column(name="image")
	public String image;
    
    public static String locationToString(int loc)
    {
        switch (loc)
        {
            case 0:
                return "Back of Head";
            case 1:
            	return "Chin";
            case 2:
            	return "Shoulder";
            case 3: 
            	return "Scapula";
            case 4:
            	return "Elbow";
            case 5:
            	return "Sacrum/coccyx";
            case 6: 
            	return "Thoracic spine";
            case 7:
            	return "Lumbar spine";
            case 8:
            	return "Trochanter";
            case 9:
            	return "Iliac crest";
            case 10:
            	return "Buttock";
            case 11:
            	return "Knee";
            case 12:
            	return "Heel";
            case 13:
            	return "Ankle";
            case 14:
            	return "Ear";
            case 15:
            	return "Cheek";
            case 16:
            	return "Nose";
            case 17:
            	return "Nostril";
            default:
                break;
        }
        return "";
    }
}
