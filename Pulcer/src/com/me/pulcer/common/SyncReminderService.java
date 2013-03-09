package com.me.pulcer.common;

import java.util.Timer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.me.pulcer.util.PLogger;
import com.the9tcat.hadi.DefaultDAO;

public class SyncReminderService extends Service {
	
	private static final int INTERVAL=(1000*30);
	private static final int UPDATE_STATE_INTERVAL=(1000*30);
	private static final int CHK_LOCAL_NOTIF_INTERVAL=(1000*15);
	
	Timer Sync_timer,status_timmer,get_detail_timmer,chk_localNotif_timmer;
	public static DefaultDAO dao;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Sync_timer=new Timer();
		status_timmer=new Timer();
		get_detail_timmer=new Timer();
		chk_localNotif_timmer=new Timer();
		dao=new DefaultDAO(getApplicationContext());
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		infoLog("Starting Sync Service command received...");

		Sync_timer.scheduleAtFixedRate(new SyncTask(getApplicationContext()), 1000, INTERVAL);
		status_timmer.scheduleAtFixedRate(new UpdateStatusTask(getApplicationContext(),dao), 0, UPDATE_STATE_INTERVAL);
		chk_localNotif_timmer.scheduleAtFixedRate(new ChkLocalNotifTask(getApplicationContext(),dao), 0, CHK_LOCAL_NOTIF_INTERVAL);
		get_detail_timmer.schedule(new GetAllDetailTask(getApplicationContext()),0);
		addGetAllDetailRecever();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		Sync_timer.cancel();
		status_timmer.cancel();
		get_detail_timmer.cancel();
		chk_localNotif_timmer.cancel();
		removeGetAllDetailRecever();
		
	}
	private void addGetAllDetailRecever(){
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PApp.ACTION_GET_ALL_DETAIL);
        registerReceiver(startGetAllDetail,intentFilter);
	}
	private void removeGetAllDetailRecever(){
		try{
			unregisterReceiver(startGetAllDetail);
		}catch(Exception e){
			
		}
	}
	BroadcastReceiver startGetAllDetail=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			infoLog("startGetAllDetail received:"+intent.getAction());
			if(intent.getAction().equalsIgnoreCase(PApp.ACTION_GET_ALL_DETAIL)){
				get_detail_timmer.schedule(new GetAllDetailTask(getApplicationContext()),0);
			}
		}
	};
	
	
	
	
	@Override
	public boolean stopService(Intent name) {
		infoLog("Stop Sync Service command received");
		
		Sync_timer.cancel();
		status_timmer.cancel();
		get_detail_timmer.cancel();
		chk_localNotif_timmer.cancel();
		
		return super.stopService(name);
	}
	
    private void infoLog(String message){
    	PLogger.getLogger().info("SyncReminderServie:=>"+message);
    }

}
