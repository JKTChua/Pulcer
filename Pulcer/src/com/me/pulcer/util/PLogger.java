package com.me.pulcer.util;

import java.util.logging.Logger;

public class PLogger {
	
	public static final String logTag="Pulcer";
	
	public static Logger getLogger(){
		 return Logger.getLogger(logTag);
	}

}
