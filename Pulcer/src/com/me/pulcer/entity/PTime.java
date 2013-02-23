package com.me.pulcer.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PTime implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int hour,min;
	public String dispTime="";
	
	
	public PTime(){
		
	}
	public PTime(int hour,int min){
		
	}
	
	public PTime(String time){
		 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
	     try {
			Date date = df.parse(time);
			this.hour=date.getHours();
			this.min=date.getMinutes();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getMin(){
		return String.format("%02d", min);
	}
	public String getHour(){
		return ""+hour;
	}
	
	
	public String getTimeStr(){
		return String.format("%02d:%02d:00",hour,min);
		
	}
	public String getDispTime(){
		String val="";
		SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat dispFormate=new SimpleDateFormat("hh:mm a");
		try {
			val=dispFormate.format(parseFormat.parse(hour+":"+min+":"+"00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return val;
	}
	
	public Date getTime(){
		Date ret=null;
		SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			ret= parseFormat.parse(hour+":"+min+":"+"00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
}
