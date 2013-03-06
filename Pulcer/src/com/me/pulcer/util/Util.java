package com.me.pulcer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.me.pulcer.common.PApp;
import com.me.pulcer.component.PTextView;
import com.me.pulcer.entity.UserDetail;
import com.me.pulcer.parser.UserDetailParser;
import com.google.gson.Gson;

public class Util
{	
	public static String getUTCTime(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		return df.format(new Date());
	}
	public static String USER_DATA_FILENAME="UserData.ser";
	public static boolean validateEmail(String email){
		String emailRegx="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[-A-Za-z0-9]+(\\.[A-Za-z]{1,})+$";
		Pattern p = Pattern.compile(emailRegx);  
	    Matcher m = p.matcher(email);
		return m.matches();
		
	}
	
	public static String getTruncatedString(String str,int compWidth,int fontsize) 
	{
		  StringBuffer buf = new StringBuffer();
		  Paint paint = new Paint();
		  paint.setTextSize(fontsize);
		  if(paint.measureText(str) <= compWidth) {
			  buf.append(str);
		  }
		  else 
		  {
			   float trunCharWidth = paint.measureText("...");
			   int availableWidthForAdd = compWidth - (int)trunCharWidth;
			   char[] city = str.toCharArray();
			   float size = 0;
			   int i=0;
			   do 
			   {
				    size = size + paint.measureText(""+city[i]);
				    
				    if(size > availableWidthForAdd)
				    	break;
				    
				    buf.append(city[i]);
				    if(i == (city.length - 1))
				    	break;
				    i++;
			   }while(size < availableWidthForAdd);
		   buf.append("...");
		   PLogger.getLogger().info("Elipsize string:"+buf.toString());
		  }
		  return buf.toString();
	}
	
	public static boolean validatePWD(String pwd){
		String emailRegx="^(?=.*[0-9])(?=.*[A-Z]).{6,}$";
		Pattern p = Pattern.compile(emailRegx);  
	    Matcher m = p.matcher(pwd);
		return m.matches();
	}
	public static void serialiseUSerData(UserDetailParser response,Context context) {
        try {
        	
        	Gson gson=new Gson();
        	String str=gson.toJson(response);
//        	PLogger.getLogger().info("Writing Data to File:"+str);
        	FileOutputStream   fos =context.openFileOutput(USER_DATA_FILENAME, Context.MODE_PRIVATE);
        	byte[] buffer=str.getBytes();
        	fos.write(buffer);
        	fos.flush();
        	fos.close();
        	
        } catch (Exception e) {
            PLogger.getLogger().log(Level.WARNING, "exception", e);
        }
    }
	
	public static UserDetailParser deSerialiseUSerData(Context context){
		UserDetailParser data=null;
		try{
			StringBuffer strBuffer=new StringBuffer();
			FileInputStream fis = context.openFileInput(USER_DATA_FILENAME);
			BufferedReader br=new BufferedReader(new InputStreamReader(fis));
			String line;
			while((line=br.readLine())!=null){
				strBuffer.append(line);
			}
			Gson gson=new Gson();
			data=gson.fromJson(strBuffer.toString(), UserDetailParser.class);
			
		}catch (Exception e) {
            PLogger.getLogger().log(Level.WARNING, "exception", e);
        };
        
        return data;
		
	}
	
	
	public static void serialiseUserList(ArrayList<UserDetail> userList,Context context) {
		try {
			FileOutputStream fout=context.openFileOutput(USER_DATA_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fout);
			os.writeObject(userList);
			os.flush();
			os.close();
		} catch (Exception e) {
			PLogger.getLogger().log(Level.WARNING, "exception", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<UserDetail> deSerialiseUserList(Context context){
		ArrayList<UserDetail> userList=null;
		try{
			FileInputStream fis = context.openFileInput(USER_DATA_FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			userList = (ArrayList<UserDetail>) is.readObject();
			is.close();
			
		}catch(Exception e){
			PLogger.getLogger().log(Level.WARNING, "exception deseralizingUserList", e);
		}
		return userList;
	}
	
	
	public static boolean validateStr(String str){
		boolean flag=false;
		if(str!=null && str.trim().length()>0 && str.equalsIgnoreCase("null")!=true){
			flag=true;
		}
		return flag;
		
	}
	
	public static void showDays(LinearLayout view,String dayString){
		PTextView []days= new  PTextView[7];
		for(int i=0;i<view.getChildCount();i++){
			days[i]=(PTextView) view.getChildAt(i);
			days[i].setVisibility(View.GONE);
		}
		Calendar cal=Calendar.getInstance();
		int dayofWeek=cal.get(Calendar.DAY_OF_WEEK);
		if(validateStr(dayString)){
			String dy[]=dayString.split("\\,");
			for(String day:dy){
				days[Integer.valueOf(day.trim())-1].setVisibility(View.VISIBLE);
			}
			if(dayofWeek==1){
				days[6].setSelected(true);
			}else{
				days[dayofWeek-2].setSelected(true);
			}
		}
		
	}
	
	public static void createRequireDirectory(){
		
		File dir=new File(PApp.BASE_DIRECTORY);
		dir.mkdirs();
		
		dir=new File(PApp.IMAGE_DIR);
		dir.mkdirs();
		
		dir=new File(PApp.VIDEO_DIR);
		dir.mkdirs();
		
	}
	
	public static String getTweelHour(String time){
		String str = null;
		try{
			SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
		    SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a");
		    str=displayFormat.format(parseFormat.parse(time));
		    
		    String sub=str.substring(str.length()-2, str.length());
		    str=str.substring(0, str.length()-2);
		    str=str+sub.toLowerCase();
		    
		    
		}catch(Exception e){
			PLogger.getLogger().info("getTweelHour:=>Error while converting time:"+e);
		}
		return str;
	}
	
	

	public static Bitmap downloadBitmap(String url) {
	    final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	    final HttpGet getRequest = new HttpGet(url);
	    Bitmap bmp=null;
	    try {
	        HttpResponse response = client.execute(getRequest);
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) { 
	            return null;
	        }
	        
	        final HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            InputStream inputStream = null;
	            try {
	                inputStream = entity.getContent();
	                BitmapFactory.Options options=new BitmapFactory.Options();
	                //options.inSampleSize=2;
	                 bmp = BitmapFactory.decodeStream(inputStream,null,options);
	            } finally {
	                if (inputStream != null) {
	                    inputStream.close();  
	                }
	                entity.consumeContent();
	            }
	        }
	    } catch (Exception e) {
	        
	        getRequest.abort();
	      
	    } finally {
	        if (client != null) {
	            client.close();
	        }
	    }
	    return bmp;
	}
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	public static boolean chkInternet(Context context){
		ConnectivityManager cm =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.CONNECTED ||
		   cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED)
		{
			return true;
		}else{
			return false;
		}
	}
	
	public static float convertDpToPixel(float dp,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi/160f);
	    return px;
	}
	
	public static float convertPixelsToDp(float px,Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
	public static String getStrPref(Context context,String key){
		SharedPreferences preferences=context.getSharedPreferences(PApp.PLUS_PREFERANCE, Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}
	public static int getIntPref(Context context,String key){
		SharedPreferences preferences=context.getSharedPreferences(PApp.PLUS_PREFERANCE, Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);
	}
	
}
