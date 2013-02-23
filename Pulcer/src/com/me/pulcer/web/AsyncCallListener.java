package com.me.pulcer.web;

public interface AsyncCallListener {

	public void onResponseReceived(String str);
	
	public void onErrorReceived(String str);
}
