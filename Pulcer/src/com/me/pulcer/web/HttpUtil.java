package com.me.pulcer.web;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.me.pulcer.common.PApp;
import com.me.pulcer.util.PLogger;

public class HttpUtil {

	public String sendJSon(String url, JSONObject obj, ArrayList<NameValuePair> params){
		
		InputStream stream = null;
		String response=null;
		try{
			HttpPost request = new HttpPost(url);
			/*StringEntity se = new StringEntity( obj.toString());  
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			request.setEntity(se);*/
			 if (!params.isEmpty()) {
	             request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	         	request.setEntity(new UrlEncodedFormEntity(params));
	         }
			 stream=executeRequest(request,url);
			 response=parseResponseToString(stream);
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public String execute(RequestMethod method,ArrayList<NameValuePair> params,String url)
	{
		InputStream stream = null;
		String response=null;
		try{
			
	    	if(method==RequestMethod.GET){
	    		 String combinedParams = "";
	    	        if (!params.isEmpty()) {
	    	            combinedParams += "?";
	    	            for (NameValuePair p : params) {
	    	                String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(),"UTF-8");
	    	                if (combinedParams.length() > 1) {
	    	                    combinedParams += "&" + paramString;
	    	                }
	    	                else {
	    	                    combinedParams += paramString;
	    	                }
	    	            }
	    	        }
	    	        PLogger.getLogger().info("Sending request:"+url+combinedParams);
	    	        HttpGet request = new HttpGet(url + combinedParams);
	    	        /*for (NameValuePair h : headers) {
	    	            request.addHeader(h.getName(), h.getValue());
	    	        }*/
	    	        stream = executeRequest(request, url);
	    	}else if(method==RequestMethod.POST){
	    		   HttpPost request = new HttpPost(url);
		           /* for (NameValuePair h : headers) {
		                request.addHeader(h.getName(), h.getValue());
		            }*/
		            if (!params.isEmpty()) {
		                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		            	request.setEntity(new UrlEncodedFormEntity(params));
		            }
		            stream = executeRequest(request, url);
	    	}
	    	response=parseResponseToString(stream);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return response;
	   
    }
	
	private InputStream executeRequest(HttpUriRequest request, String url) 
	throws UnknownHostException, Exception
	{
    	InputStream instream = null;
    	HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = PApp.CONNECTION_TIMEOUT;
		
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		
		int timeoutSocket = PApp.CONNECTION_TIMEOUT;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                instream = entity.getContent();
            }
        }
        catch(UnknownHostException ex){
        	PLogger.getLogger().info("Error on executing request UNKONW_HOST:"+ex);
        	ex.printStackTrace();
        	client.getConnectionManager().shutdown();
        	throw new UnknownHostException("Server in not reachable. Please check internet connection...");
        }
        catch (Exception e) {
        	  PLogger.getLogger().info("Error on executing request:"+e);
        	  e.printStackTrace();
            client.getConnectionManager().shutdown();
            throw new Exception(e);
        }
        return instream;
    }
	
	private String parseResponseToString (InputStream stream) {
    	StringBuffer buf = new StringBuffer();
    	try {
    		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	     	   buf.append(line);
	        }
    	}
    	catch(Exception ex) {
    		buf.append(ex.getMessage());
    	}
    	return buf.toString();
    }
}
