package com.me.pulcer.common;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;

import com.getplusapp.mobile.android.R;
import com.getplusapp.mobile.android.util.PLogger;

public class CameraManager {
	
	public static final int CAMERA_REQUEST_CODE= 10001;
	public static final int GALLERY_REQUEST_CODE= 10002;
	public static final int VIDEO_REQUEST_CODE= 10003;
	
	
	Activity activity;
	Dialog dlg;
	boolean isVideo=false;
	
	public CameraManager(Activity activity){
		this.activity=activity;
		
	}
	public String captureImage(){
		isVideo=false;
		String fileName=PApp.IMAGE_DIR+generateFileName(true);
		showDialog(fileName);
		return fileName;
	}
	
	public void showDialog(final String fileName){
		dlg=new Dialog(activity,R.style.PlusDialog);
		dlg.setContentView(R.layout.camera_dialog);
		dlg.findViewById(R.id.dlg_cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dlg.dismiss();
			}
		});
		dlg.findViewById(R.id.from_cam_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadCamera(fileName);
				dlg.dismiss();
			}
		});
		dlg.findViewById(R.id.from_gallery_btn).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadGallery();
				dlg.dismiss();
			}
		});
		WindowManager.LayoutParams WMLP=dlg.getWindow().getAttributes();
		WMLP.width=WindowManager.LayoutParams.FILL_PARENT;
		dlg.getWindow().setAttributes(WMLP);
		dlg.show();
		
		
		
	}
	private void loadCamera(String file){
		if(isVideo!=true){
			PLogger.getLogger().info("Capturing IMAGE:"+file);
			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(file)));
			activity.startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
		}
		
	}
	
	private void loadGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		activity.startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE);
	}
	
	
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	private String generateFileName(boolean isImage){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dateStr = displayFormat.format(cal.getTime());
		String fileName="";
		
		if(isImage){
			fileName="IMG_"+dateStr+".PNG";
		}else{
			fileName="VIDEO_"+dateStr+".MP4";
		}
		
		return fileName;
		
	}
	
	public Bitmap setImgOrienetation(String path) {
		File f = new File(path);
		Bitmap correctBmp=null;
        ExifInterface exif = null;
		try {
			exif = new ExifInterface(f.getPath());
		} catch (IOException e) {			
			e.printStackTrace();
		}
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        PLogger.getLogger().info("Camera manager======> orientation:"+orientation);
        
        int angle = 0;

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            angle = 90;
        } 
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
            angle = 180;
        } 
        else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            angle = 270;
        }

        Matrix mat = new Matrix();
        mat.postRotate(angle);
        Bitmap bmp;
		try {
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inPurgeable=true;
			options.inSampleSize=2;
			bmp=BitmapFactory.decodeStream(new FileInputStream(f), null, options);
			correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
			FileOutputStream fout=new FileOutputStream(f);
			correctBmp.compress(Bitmap.CompressFormat.JPEG, 90, fout);
		} catch (Exception e) {
			
			//TODO Add Acra Code 
			PLogger.getLogger().info("setImgOrienetation error"+ e);
		}
		return correctBmp;
        
	}
	
	public void compressBitmap(String file)
    {
    	Bitmap bitmap;
    	FileOutputStream fos;
    	try
    	{
    		BitmapFactory.Options options = new BitmapFactory.Options();
			options.inTempStorage = new byte[16*1024];
			options.inSampleSize = 4;
    		
    		bitmap=BitmapFactory.decodeFile(file,options);
    		fos = new FileOutputStream(file);
    		bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
    		fos.close();
    		
    		bitmap.recycle();
    		bitmap=null;
    		System.gc();
    		
    		
    	}catch (Exception e) {
			PLogger.getLogger().info("CompressBitmap:Error:-"+e);
		}
    	
    	
    }
	public void compressImage(String fileName){
		pictureCompressTask task=new pictureCompressTask(fileName, activity);
		task.execute();
	}
	
	private class pictureCompressTask extends AsyncTask<Void, Void, Void>
    {
    	String file;
    	boolean isRunning=false;
    	Context context;
    	public pictureCompressTask(String photoFile,Context context) {
    		this.file=photoFile;
    		this.context=context;
		}
    	ProgressDialog dialog;
    	@Override
    	protected void onPreExecute() {
    		
    		try{
    			dialog=new ProgressDialog(context);
        		dialog.setCancelable(false);
        		dialog.setMessage("Compressing Image please wait");
        		dialog.show();
    		}catch (Exception e) {
    		
			}
    		
    		
    		super.onPreExecute();
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				isRunning=true;
				compressBitmap(file);
			}catch(Exception e)
			{
				isRunning=false;
				dismishDialog();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			isRunning=false;
			dismishDialog();
			super.onPostExecute(result);
		}
		void dismishDialog()
		{
			if(isRunning!=true){
				try
				{
					dialog.dismiss();
				}catch (Exception e) {
				}
				isRunning=false;
			}
		}
    	
    }
	

}
