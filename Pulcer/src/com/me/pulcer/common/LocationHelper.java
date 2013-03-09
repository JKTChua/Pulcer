package com.me.pulcer.common;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.me.pulcer.util.PLogger;

public class LocationHelper
{
	LocationManager locationManager;
	private LocationResult locationResult;
	boolean gpsEnabled = false;
	boolean networkEnabled = false;
	Criteria criteria;
	public boolean getLocation(Context context, LocationResult result)
	{       
	    locationResult = result;
	    
	    	boolean returnVal=false;
	    
		    criteria=new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	       /* try{
	        	PLogger.getLogger().info("LOC Setting Accuracy:"+accuracy);
	        	criteria.setAccuracy(accuracy);
	        }catch (IllegalArgumentException e) {
	        	PLogger.getLogger().info("%%%%%%%% ERROR WHILE SETTING ACCURACY.ACCURACY IS SET TO FINE %%%%%%%%%%");
	        	
			}*/
	
	    	if(locationManager == null)
	    	{
	    		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    	}
	        try
	        {
	            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	            
	            if(gpsEnabled)
		        {
	            	returnVal=true;
	            	PLogger.getLogger().info("Retriving Location with GPS");
		        	String provider=locationManager.getBestProvider(criteria, true);
		        	locationManager.requestLocationUpdates(provider, 0, 0, locationListenerGps);
		            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		        }
	        }
	        catch (Exception ex) {
	        	returnVal=false;
	        	PLogger.getLogger().info("Error  while GPS:"+ex);
	        }
	        
	        try  {
	        	PLogger.getLogger().info("into network locaton");
	            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	            if(networkEnabled)
		        {
	            	returnVal=true;
	            	PLogger.getLogger().info("Retriving Location with NETWORK");
		        	String provider=locationManager.getBestProvider(criteria, true);
		            locationManager.requestLocationUpdates(provider, 0, 0, locationListenerNetwork);
		        }
	        }
	        catch (Exception ex) {
	        	returnVal=false;
	        	PLogger.getLogger().info("Error  while network:"+ex);
	        }
	       return returnVal;
	}
	
	LocationListener locationListenerGps = new LocationListener() {
	    public void onLocationChanged(Location location)
	    {
	        locationResult.gotLocation(location);
	        locationManager.removeUpdates(this);
	        locationManager.removeUpdates(locationListenerNetwork);
	
	    }
	    public void onProviderDisabled(String provider) {}
	    public void onProviderEnabled(String provider) {}
	    public void onStatusChanged(String provider, int status, Bundle extra) {}
	};
	
	LocationListener locationListenerNetwork = new LocationListener() {
	    public void onLocationChanged(Location location)
	    {
	        locationResult.gotLocation(location);
	        locationManager.removeUpdates(this);
	        locationManager.removeUpdates(locationListenerGps);
	
	    }
	    public void onProviderDisabled(String provider) {}
	    public void onProviderEnabled(String provider) {}
	    public void onStatusChanged(String provider, int status, Bundle extra) {}
	
	};
	
//	
	
	public void stopLocationUpdates() {
		if(locationManager!=null){
			  locationManager.removeUpdates(locationListenerGps);
			   locationManager.removeUpdates(locationListenerNetwork);
		}
	  
	}
	
	
	public static abstract class LocationResult
	{
	    public abstract void gotLocation(Location location);
	}
	
}