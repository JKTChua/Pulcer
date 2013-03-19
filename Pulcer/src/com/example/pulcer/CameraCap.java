package com.example.pulcer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraCap extends Activity 
{
	private static final String TAG = "CameraDemo";
//	Camera camera;
	Preview preview;
	Button buttonClick;
	Intent intent;
	
//	public CameraCap(Intent i)
//	{
//		i.getStringExtra("path");
//	}
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		
		intent = getIntent();
		
		preview = new Preview(this);
//		preview.scrollTo(400, 400);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		
		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				try
				{
					Log.d(TAG, "taking picture");
					preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
					Log.d(TAG, "killing camera");
				}
				catch(Exception e)
				{
					e.printStackTrace();
					
				}
				finally
				{
					preview.killCamera();
					Log.d(TAG, "camera killed");
				}
				
			}
		});

		Log.d(TAG, "onCreate'd");
	}

	ShutterCallback shutterCallback = new ShutterCallback() 
	{
		public void onShutter() 
		{
			Log.d(TAG, "onShutter'd");
		}
	};

	/** Handles data for raw picture */
	PictureCallback rawCallback = new PictureCallback() 
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Log.d(TAG, "onPictureTaken - raw");
		}
	};

	/** Handles data for jpeg picture */
	PictureCallback jpegCallback = new PictureCallback() 
	{
		public void onPictureTaken(byte[] data, Camera camera) 
		{
			FileOutputStream outStream = null;
			try 
			{
				
				// write to local sandbox file system
				File file = new File(intent.getStringExtra("path"), 
						intent.getIntArrayExtra("ids")[0] + "-" + intent.getIntArrayExtra("ids")[1] + ".jpg");
				outStream = new FileOutputStream(file.getAbsolutePath());
//				 outStream = CameraCap.this.openFileOutput(file.getAbsolutePath(), 0);
				// Or write to sdcard
//				outStream = new FileOutputStream(String.format(
//						"/sdcard/%d.jpg", System.currentTimeMillis()));
				outStream.write(data);
				outStream.close();
//				preview.killCamera();
				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			finally 
			{
			}
			Log.d(TAG, "onPictureTaken - jpeg");
			finish();
		}
	};
	
	protected void onPause()
	{
//		preview.killCamera();
		super.onPause();
	}
}