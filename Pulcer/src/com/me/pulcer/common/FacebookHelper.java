package com.me.pulcer.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.getplusapp.mobile.android.util.PLogger;


public class FacebookHelper {
	
	public Facebook facebook;
	static String FB_APPID="306349812766756";
	static String FB_PERMISSIONS []={"read_stream","user_birthday","publish_checkins","offline_access","email"};
	ProgressDialog pDialog;
	FacebookHelper.myFBListener customListener;
	
	
	public FacebookHelper() {
		facebook=new Facebook(FB_APPID);
	}
	public void login_core(Context context,FacebookHelper.myFBListener listener){
		customListener=listener;
		facebook.authorize((Activity) context, FB_PERMISSIONS,fbCoreListener);
	}
	public void login(Context context,FacebookHelper.myFBListener listener){
		customListener=listener;
		if(facebook!=null){
			if(facebook.isSessionValid()!=true){
				facebook.authorize((Activity) context, FB_PERMISSIONS,fbCoreListener);
			}else{
				listener.onComplete("SessionValid");
			}
		}
	}
	public boolean isSessionValid(){
		return facebook.isSessionValid();
	}
	public void logout(Context context){
		try {
			facebook.logout(context);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	Facebook.DialogListener fbCoreListener=new Facebook.DialogListener() {
		
		@Override
		public void onFacebookError(FacebookError arg0) {
			customListener.onError(arg0.getMessage());
		}
		
		@Override
		public void onError(DialogError arg0) {
			customListener.onError(arg0.getMessage());
		}
		
		@Override
		public void onComplete(Bundle arg0) {
			customListener.onComplete(arg0.toString());
			
		}
		@Override
		public void onCancel() {
		
			
		}
	};
	
	void infolog(String msg){
		PLogger.getLogger().info(msg);
	}
	
	public void postToWall(Context context,String message,Bitmap bmp,String type,myFBListener listener){
		PostWall task=new PostWall(context);
		task.setListener(listener);
		task.setPostMessage(message, bmp,type);
		task.execute();
	}
	
	public void getUserDetail(Context context,myFBListener listener){
		UserDetailTask task=new UserDetailTask(context);
		task.setListener(listener);
		task.execute();
	}
	
	public class PostWall extends AsyncTask<String, Void, Void>{
		
		Context context;
		ProgressDialog pDialog;
		String postMessage="";
		String layoutType="";
		myFBListener listener;
		boolean isError=false;
		String errorMessage;
		String response="";
		Bitmap bmp;
		public PostWall(Context context){
			this.context=context;
			pDialog=new ProgressDialog(context);
			
		}
		void setListener(myFBListener listener){
			this.listener=listener;
		}
		void setPostMessage(String msg,Bitmap bmp,String type){
			this.postMessage=msg;
			this.bmp=bmp;
			this.layoutType=type;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Please wait, posting on Facebook ");
			pDialog.show();
		}
		@Override
		protected Void doInBackground(String... params) {
			Bundle bundle=new Bundle();
			try{
				String uName="";
				String uData=facebook.request("me");
				if(uData!=null && uData.length()>0 ){
					JSONObject obj=new JSONObject(uData);
					uName=obj.getString("name");
				}
				String caption=String.format("%s has done a %s using Osho Zen Tarot app. http://osho.com/mobile/\n\n %s", uName,this.layoutType,this.postMessage);
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				bmp.compress(CompressFormat.PNG, 90, bout);
				bundle.putString("caption", caption);
				bundle.putByteArray("picture",bout.toByteArray());
				
				infolog("ByteArraySize:"+bout.toByteArray().length);
				infolog("Befor posting bundle:"+bundle.toString());
				response=facebook.request("me/photos", bundle,"POST");
				
				if(response!=null && response.length()>0){
					JSONObject resp=new JSONObject(response);
					if(resp.getString("id")==null || resp.getString("post_id")==null){
						isError=true;
						errorMessage=response;
					}
				}else{
					isError=true;
					errorMessage="Blank Response from Facebook";
				}
				infolog("FBRESPonse:"+response);
				
			} catch (MalformedURLException e) {
				isError=true;
				errorMessage=e.getMessage();
						
				e.printStackTrace();
			} catch (IOException e) {
				isError=true;
				errorMessage=e.getMessage();
				e.printStackTrace();
			} catch (JSONException e) {
				isError=true;
				errorMessage=e.getMessage();
				e.printStackTrace();
			}
			
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismissDialog();
			if(isError){
				listener.onError(errorMessage);
			}else
			{
				listener.onComplete(response);
				//infolog("FACEBOOK ::::: successfully posted");
				showMessageDialog(context, "Facebook", "Successfully posted on Facebook.");
			}
		}
		
		void dismissDialog(){
			if(pDialog.isShowing())
				pDialog.dismiss();
		}
		
	}
	public static interface myFBListener{
		
		public void onComplete(String response);
		public void onError(String errorMessage);
		
	}
	public  class UserDetailTask extends AsyncTask<String, Void, Void>{
		Context context;
		ProgressDialog pDialog;
		myFBListener listener;
		boolean isError=false;
		String errorMessage;
		String response="";
		
		public UserDetailTask(Context context){
			this.context=context;
			pDialog=new ProgressDialog(context);
			
		}
		public void setListener(myFBListener listener){
			this.listener=listener;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Please wait, Retriving Data...");
			pDialog.show();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			if(facebook!=null){
				try {
					response=facebook.request("me");
					
					Bundle bundle=new Bundle();
		    		bundle.putString("fields", "picture");
		    		bundle.putString("type", "large");
		    		String photo=facebook.request("me", bundle);
		    		String photoURL=new JSONObject(photo).getString("picture");
		    		JSONObject temp=new JSONObject(response);
		    		temp.put("picture_url", photoURL);
		    		response=temp.toString();
					
				} catch (MalformedURLException e) {
					isError=true;
					errorMessage=e.getMessage();
				} catch (IOException e) {
					isError=true;
					errorMessage=e.getMessage();
				}catch (JSONException e) {
					isError=true;
					errorMessage=e.getMessage();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			dismissDialog();
			if(isError){
				listener.onError(errorMessage);
			}else{
				listener.onComplete(response);
			}
		}
		
		void dismissDialog(){
			if(pDialog.isShowing())
				pDialog.dismiss();
		}
		
	}
	
	
	
	protected final void showMessageDialog(Context contex,String title, String message) {
		if(message != null && message.trim().length() > 0) {
			Builder builder = new AlertDialog.Builder(contex);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
				}
			});
			builder.show();
		}
	}
	
	

}
