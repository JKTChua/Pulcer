package com.me.pulcer.common;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.me.pulcer.entity.UpdateStatus;
import com.me.pulcer.util.PLogger;
import com.me.pulcer.util.Util;
import com.me.pulcer.web.HttpUtil;
import com.google.gson.Gson;
import com.the9tcat.hadi.DefaultDAO;

public class UpdateStatusTask extends TimerTask{

	ArrayList<NameValuePair> params;
	DefaultDAO dao;
	Context context;
	private HttpUtil httpUtill;
	
	public UpdateStatusTask(Context context,DefaultDAO dao){
		params=new ArrayList<NameValuePair>();
		httpUtill=new HttpUtil();
		this.context=context;
		this.dao=dao;
	}
	
	
	@Override
	public void run() {
		params.clear();
		try {
			params.add(new BasicNameValuePair("access_token", Util.getStrPref(context,PApp.Pref_AccessToken)));
			JSONArray list=getData();
			if(list.length()>0){
				JSONObject obj=new JSONObject();
				obj.put("jsonData", getData());
				infoLog("JSONArray:"+obj.toString());
				params.add(new BasicNameValuePair("jsonData", obj.toString()));
				infoLog("StatusUpdate Response:=>"+httpUtill.sendJSon(PApp.WEB_SERVICE_URL+"multiplestatusupdate",obj,params));
				removeData(list);
			}

		} catch (Exception e) {
			infoLog("Error while updateStatus");
			e.printStackTrace();
		}
	}
	
	public JSONArray getData(){
		JSONArray array=null;
		try{
			DefaultDAO dao=new DefaultDAO(context);
//			infoLog("Retriving data fro db:");
			array=new JSONArray();
			Gson gson=new Gson();
			String []args={"1"};
			
			@SuppressWarnings("unchecked")
			List<UpdateStatus> list=(List<UpdateStatus>) dao.select(UpdateStatus.class, false, "reminder_id>?", args, null, null, null, null);
			if(list!=null && list.size()>0){
				for(UpdateStatus state:list){
					array.put(new JSONObject(gson.toJson(state)));
				}
			}
		}catch(Exception e){
			infoLog("Error while fetching status"+e);
		}
		
		
		return array;
	}
	public void removeData(JSONArray array){
		try{
			Gson gson=new Gson();
			ArrayList<UpdateStatus> list=new ArrayList<UpdateStatus>();
			String idList="";
			for(int i=0;i<array.length();i++){
				list.add(gson.fromJson(array.get(i).toString(), UpdateStatus.class));
				idList+=""+list.get(i).reminderId+",";
			}
			if(idList.endsWith(",")){
				idList=idList.substring(0, idList.length()-1);
			}
			String args[]={"in("+idList+")"};
			infoLog("Delete Condition:"+args[0]);
			dao.delete(UpdateStatus.class, " reminder_id "+args[0], null);
		}catch(Exception e){
			infoLog("Error while deleting recodrds:"+e);
		}
	}
	private void infoLog(String message){
		PLogger.getLogger().info("UpdateStatusTask=>"+message);
		
	}
}
