package com.me.pulcer.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.the9tcat.hadi.annotation.Column;
import com.the9tcat.hadi.annotation.Table;

@Table(name="ulcer")
public class UlcerEnt implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("id")
	@Column(primary=true,name="ulcer_id")
	public int ulcerId;
	
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
	public byte location;
	
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
	public byte locationQualifier;
	
	
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
    
    public String locationToString(byte loc)
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
