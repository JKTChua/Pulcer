package com.me.pulcer.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class Item implements Serializable{

	/**
	 * 
	 */
	
	public static int EVERYDAY=101;
	public static int WEEKENDS=103;
	public static int WORKDAYS=102;
	public static int CUSTOM=100;
	
	public static int MODE_DEFAULT=1000;
	public static int MODE_DOSAGE=1001;
	public static int MODE_FREQ=1002;
	public static int MODE_TIME=1003;
	public static int MODE_USER=1004;
	
	
	private static final long serialVersionUID = 1L;
	public String dosageContent;
	public int dosageContentIndex;
	public int dosageTimes;
	public String selecetedDays;
	public int selectedDaysMode;
	public PTime time;
	public int timeHourDuration;
	public int dlgMode=MODE_DEFAULT;
	public String dosageValues;
	
	public boolean isToCreatePickerValue=false;
	
	public ArrayList<UserDetail> userList=new ArrayList<UserDetail>();
	
	public String getDaysString(){
		String ret="";
		try{
			
			if(selecetedDays!=null){
				if(selecetedDays.contains(",")){
					String list[]=selecetedDays.split(",");
					if(list!=null){
						for(int i=0;i<list.length;i++){
							int day=Integer.valueOf(list[i]);
							ret+=getDayStr(day);
						}
					}
				}else{
					int day=Integer.valueOf(selecetedDays);
					ret=getDayStr(day);
				}
			}
			if(ret.endsWith(",")){
				ret=ret.substring(0, ret.length()-1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ret;
	}
	public String getDayStr(int day){
		String ret="";
		switch (day) {
		case 1:
			ret+="Mo,";
			break;
		case 2:
			ret+="Tu,";
			break;
		case 3:
			ret+="We,";
			break;
		case 4:
			ret+="Thu,";
			break;
		case 5:
			ret+="Fri,";
			break;	
		case 6:
			ret+="Sat,";
			break;
			
		case 7:
			ret+="Su,";
			break;
	
		}
		return ret;
	}
	
}
