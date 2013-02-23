package com.me.pulcer.web;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.me.pulcer.common.PApp;
import com.me.pulcer.util.PLogger;

public class AsyncCall extends AsyncTask<Void, Void, String> {
	
	 public RequestMethod webMethod = RequestMethod.GET;
	 private ArrayList<NameValuePair> params;
	 private ArrayList<NameValuePair> headers;
	    
	 public boolean isRunning = false;
	 public boolean isCallCompleted = false;
	 public boolean isCancelled =false;
	 public boolean isTouploadFile=false;
	 
	 
	 public  ProgressDialog pDialog;
	 public String pbarMessage = "Loading please wait...";
	 private boolean isProgressbar;
	 private String url;
	 
	
	 private AsyncCallListener listener;
	 private String errorMessage;
	 private boolean isError = false;
	 private Hashtable<String, File> fileList=new Hashtable<String, File>();
	 
	
	 public AsyncCall(Context context) {
		 pDialog = new ProgressDialog(context);
		 pDialog.setMessage(pbarMessage);
		 pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				cancel(true);
			}
		 });
		 
		 params = new ArrayList<NameValuePair>();
		 headers = new ArrayList<NameValuePair>();
	 }
	
	public void showProgressBar(boolean flag)  {
		isProgressbar=flag;
		if (!flag)
			this.pDialog = null;
	}
	
	public void addFile(String key,File file){
		fileList.put(key, file);
		
	}
	
	public boolean getIsProgressbar() {
		return isProgressbar;
	}
	
	public void setMessage(String message) {
		if (pDialog != null)
			pDialog.setMessage(message);
	}
    
	public void setUrl(String url) {
    	this.url = url;
    }
    
    public void addParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }
 
    
    public void addHeader(String name, String value) {
        headers.add(new BasicNameValuePair(name, value));
    }
	
	public void setAsyncCallListener(AsyncCallListener listener) {
		this.listener = listener;
	}
	@Override
	protected void onPreExecute() {
		showProgressbar();
	}
	
	@Override
	protected void onCancelled() {
		pDialog.dismiss();
		super.onCancelled();	
		isCancelled = true;
	}
	
	@Override
	protected String doInBackground(Void... args) {
		String response = null;
		try {
			isRunning=true;
			addParam("device_type", "Android");
			InputStream stream;
			if(isTouploadFile!=true)
				stream = execute(webMethod);
			else{
				stream=executeWithMulitipart();
			}
			
			isCallCompleted = true;
	        isRunning = false;
	        response = parseResponseToString(stream);
	        stream.close();
		}
		catch(UnknownHostException exp){
			isError = true;
			errorMessage =exp.getMessage(); 
			isCallCompleted = true;
			isRunning = false;
		}
		catch(Exception ex) {
			isError = true;
			errorMessage = ex.getMessage();
			isCallCompleted = true;
			isRunning = false;
		}
		return response;
	}
	
	@Override
	protected void onPostExecute(String response) {
		try {
			if (pDialog != null)
				pDialog.dismiss();
			if(listener != null) {
				if(isError)
					listener.onErrorReceived(errorMessage);
				else
					listener.onResponseReceived(response);
			}
		}
		catch (Exception e) {
			PLogger.getLogger().info("Error onPostExecute while dismissing dialog: "+e);
			e.printStackTrace();
		
		}
		
	}
	
    private void showProgressbar() {
		if(!isCancelled && pDialog != null) {
			if(pDialog.isShowing()!=true){
				pDialog.show();
			}
		}
		
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
    
    public InputStream executeWithMulitipart() throws Exception{
    	MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
    	HttpPost request = new HttpPost(url);
    	InputStream stream = null;
    	PLogger.getLogger().info("Sending Multipart:"+fileList.size());
    	if(fileList.isEmpty()!=true){
    		Enumeration<String> enumKey = fileList.keys();
    		
    		ByteArrayOutputStream out;
    		FileInputStream fin;
    		byte []buffer=new byte[1024];
    		
    		while(enumKey.hasMoreElements()){
    			String key=enumKey.nextElement();
    			PLogger.getLogger().info("Adding file:"+key);
    			fin=new FileInputStream(fileList.get(key));
    			out=new ByteArrayOutputStream();
    			while(fin.read(buffer)!=-1){
    				out.write(buffer);
    			}
    			entity.addPart(key,(ContentBody) new ByteArrayBody(out.toByteArray(), "image/jpeg"));
    			out.flush();
    			fin.close();
    			out.close();
    			
    		}
    	}
    	
    	/*if (!params.isEmpty()) {
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        	request.setEntity(new UrlEncodedFormEntity(params));
        }*/
    	if (!params.isEmpty()) {
    		for(NameValuePair data:params){
    			PLogger.getLogger().info("Adding Param:"+data.getName()+":"+data.getValue());
    			entity.addPart(data.getName(), new StringBody(data.getValue()));
    		}
    	}
    	request.setEntity(entity);
    	stream=executeRequest(request,url);
    	return stream;
    	
    }

    public InputStream execute(RequestMethod method) throws UnknownHostException,Exception {
    	InputStream stream = null;
        switch (method) {
	        case GET: {
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
	            for (NameValuePair h : headers) {
	                request.addHeader(h.getName(), h.getValue());
	            }
	 
	            stream = executeRequest(request, url);
	            break;
	        }
	        case POST: {
	        	PLogger.getLogger().info("URL:"+url);
	        	for(NameValuePair pair:params){
	        		PLogger.getLogger().info("Name:"+pair.getName()+" value:"+pair.getValue());
	        	}
	            HttpPost request = new HttpPost(url);
	            for (NameValuePair h : headers) {
	                request.addHeader(h.getName(), h.getValue());
	            }
	            if (!params.isEmpty()) {
	                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	            	request.setEntity(new UrlEncodedFormEntity(params));
	            }
	            stream = executeRequest(request, url);
	            break;
	        }
		default:
			break;
        }
        
        return stream;
    }
 
    private InputStream executeRequest(HttpUriRequest request, String url) throws UnknownHostException, Exception
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
        }catch (SocketException e) {
        	PLogger.getLogger().info("Socket Exception request:"+e);
        	e.printStackTrace();
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
}
